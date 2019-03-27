package moe.feo.NetConsole;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.gpio.GpioProviderPinCache;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiGpioProvider;
import com.pi4j.io.gpio.RaspiPin;

public class CLI implements Runnable {

	private static CLI instance;

	public Thread t;
	public Terminal terminal;
	public LineReader linereader;

	private CLI() {
		try {
			this.terminal = TerminalBuilder.builder().system(true).build(); // 终端
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.linereader = LineReaderBuilder.builder().terminal(terminal).build(); // linereader
	}

	public static synchronized CLI getInstance() {
		if (instance == null) {
			CLI.instance = new CLI();
		}
		return instance;
	}

	public void startTheard() {
		if (t == null) {
			t = new Thread(this);
		}
		t.start();
		Printer.getInstance().printInfo(Message.CLISTARTED.getMessage());
	}

	@Override
	public void run() {
		while (true) {
			String line = null;
			try {
				line = linereader.readLine("> "); // 命令提示符
			} catch (UserInterruptException e) { // 用户ctrl+c停止
				break;
			} catch (EndOfFileException e) { // 用户ctrl+d停止
				line = "stop";
			}
			String[] args = line.split("\\s+"); // 空格分割
			OnCommand(args);// 这里把参数传给其他方法
			if (args[0].equals("stop")) {
				break;
			}
		}
	}

	public void OnCommand(String[] args) {
		switch (args[0]) {
		case "switch": // 开关, 格式: switch <引脚名> <on|off>
		{
			Switch sw = null;
			boolean on = false;
			if (args[1] != null) {
				sw = new Switch(String.valueOf(args[1]));// 创建开关对象
			}
			if (args[2].equals("on")) {
				on = true;
			} else if (args[2].equals("off")) {
				on = false;
			} else {
				Printer.getInstance().printWarn(Message.INVALIDUSAGE.getMessage());
				break;
			}
			if (on) {
				if (sw.On()) {
					Printer.getInstance().printInfo(Message.PINOPENED.getMessage());
				}
			} else if (!on) {
				if (sw.Off()) {
					Printer.getInstance().printInfo(Message.PINCLOSED.getMessage());
				}
			}
			break;
		}
		case "timing": // 定时开关, 格式: timing <引脚名> <on|off> <yyyy-MM-dd_hh:mm:ss> [loop]
		{
			boolean loop = false;
			boolean on = false;
			Date date = null;
			Switch sw = new Switch(args[1]);// 创建开关对象
			if (args[2].equals("on")) {
				on = true;
			} else if (args[2].equals("off")) {
				on = false;
			} else {
				Printer.getInstance().printWarn(Message.INVALIDUSAGE.getMessage());
				break;
			}
			date = Tools.toDate(args[3]);
			if (args[4].equals("loop")) {
				loop = true;
			}
			if (date != null) {
				sw.Inertia(date, on, loop);
				Printer.getInstance().printInfo(Message.TIMERCREATED.getMessage());
			}
			break;
		}
		case "info": { // 查看所有pin口状态, 格式: info [pincache|pin]
			try {
				switch (args[1]) {
				case "pincache": {
					Printer.getInstance().printInfo(Message.PINCACHESTATE.getMessage());
					String message = new String();
					for (int x = 0; x < PinCache.listpincache.size(); x = x + 1) {
						GpioProviderPinCache pin = PinCache.listpincache.get(x);
						message = message + RaspiPinList.getInstance().getNameByPin(PinCache.getPinByCache(pin)) + ":"
								+ pin.getMode().getName() + "/" + pin.getState().getName() + "; ";
					}
					Printer.getInstance().printInfo(message);
					break;
				}
				case "pin": {
					Printer.getInstance().printInfo(Message.PINSTATE.getMessage());
					String message = new String();
					GpioProvider provider = new RaspiGpioProvider();
					for (int x = 0; x < 32; x = x + 1) {
						Pin pin = RaspiPin.getPinByAddress(x);
						message = message + RaspiPinList.getInstance().getNameByPin(pin) + ":"
								+ provider.getMode(pin).getName() + "/" + provider.getState(pin).getName() + "; ";
					}
					Printer.getInstance().printInfo(message);
					break;
				}
				default: {
					Printer.getInstance().printWarn(Message.INVALIDUSAGE.getMessage());
				}
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				String message = new String(Message.EQUIPSTATES.getMessage());
				GpioProvider provider = new RaspiGpioProvider();
				for (Map.Entry<String, Equipment> entry : Equipment.map.entrySet()) {
					Equipment equip = entry.getValue();
					message = message + System.getProperty("line.separator") + equip.name + "-" + equip.type + ": "
							+ provider.getState(equip.pin) + ";";
				}
				if (Equipment.map.isEmpty()) {
					message = message + System.getProperty("line.separator") + "null";
				}
				Printer.getInstance().printInfo(message);
			}
			break;
		}
		case "reload": {
			RaspiPinList.getInstance().Reload();
			Tools.config.Reload();
			Message.Reload();
			Equipment.Reload();
			Printer.getInstance().printInfo(Message.RELOADED.getMessage());
			break;
		}
		case "stop": {
			Printer.getInstance().printInfo(Message.GOODBYE.getMessage());
			Thread stopthread = new Thread(new Runnable() { // 创建这个线程当作关闭其他线程的线程
				@Override
				public void run() {
					Equipment.Unload(); // 退出所有定时器
					if (Tools.config.getBoolean("initialize.cancel")) {
						Tools.Initialize(); // 初始化引脚
					}
				}
			});
			stopthread.start();
			SimpleDateFormat ft = new SimpleDateFormat("HH:mm:ss");
			Date date = new Date();
			System.out.println("[" + ft.format(date) + " INFO" + "]: " + Message.GOODBYE.getMessage());
		}
		default: {
			Printer.getInstance().printWarn(Message.INVALIDUSAGE.getMessage());
		}
		}
	}

}
