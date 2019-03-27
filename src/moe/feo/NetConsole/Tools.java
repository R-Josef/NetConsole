package moe.feo.NetConsole;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class Tools {

	public static Config config = new Config("config.yml"); // 主配置文件

	// 初始化的方法

	public static void Initialize(Pin pin) { // 初始化单个pin
		int x = pin.getAddress();
		GpioController controller = GpioFactory.getInstance();
		GpioPinDigitalInput gpiopin = controller.provisionDigitalInputPin(pin); // 初始化pin为输入模式
		PinCache.listpincache.get(x).setMode(PinMode.DIGITAL_INPUT); // 写入模式为输入模式
		if ((x == 8) || (x == 9) || (x == 7) || (x == 30) || (x == 21) || (x == 22) || (x == 16) || (x == 10)
				|| (x == 11) || (x == 31)) { // 这几个是高电平加上拉电阻
			PinCache.listpincache.get(x).setState(PinState.HIGH); // 虽然输入引脚的State会变，但是貌似有个隐藏的初始状态
			gpiopin.setPullResistance(PinPullResistance.PULL_UP);
			PinCache.listpincache.get(x).setResistance(PinPullResistance.PULL_UP);
		} else {
			PinCache.listpincache.get(x).setState(PinState.LOW);
			gpiopin.setPullResistance(PinPullResistance.PULL_DOWN);
			PinCache.listpincache.get(x).setResistance(PinPullResistance.PULL_DOWN);
		}
		controller.unprovisionPin(gpiopin);
	}

	public static void Initialize() { // 初始化全部pin
		for (int x = 0; x < PinCache.listpincache.size(); x++) {
			Initialize(RaspiPin.getPinByAddress(x));
		}
	}

	// 时间转换的方法

	public static Date toDate(String date) {
		if (date.contains("-") && date.contains(":")) { // 将长时间转化为系统时间并返回
			SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
			Date Date = new Date();
			try {
				Date = ft.parse(date);
				return Date;
			} catch (ParseException e) {
				Printer.getInstance().printWarn(Message.INVALIDDATE.getMessage());
				return null;
			}
		} else if (date.contains(":")) { // 将HH:mm:ss时间转化为完整时间并返回
			SimpleDateFormat shortdateft = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat fulldateft = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
			Date now = new Date();
			String strnow = shortdateft.format(now); // 当前时间的短时间
			String fulldate = strnow + "_" + date; // 输入的完整时间的str
			try {
				return fulldateft.parse(fulldate); // 返回转化为date的完整时间
			} catch (ParseException e) {
				Printer.getInstance().printWarn(Message.INVALIDDATE.getMessage());
				return null;
			}
		} else {
			Printer.getInstance().printWarn(Message.INVALIDDATE.getMessage());
			return null;
		}
	}

}
