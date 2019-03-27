package moe.feo.NetConsole;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;

public class Equipment {

	public String name;
	public Pin pin;
	public String type;
	public List<String> timing;
	public GpioPin gpiopin;
	public List<Timer> listtimer = new ArrayList<>(); // 定时器的合集

	public static Map<String, Equipment> map = new LinkedHashMap<String, Equipment>(); // 用电器的合集
	public static Config yml = new Config("equipment.yml");

	public Equipment(String name, Pin pin, String type, List<String> timing) {
		this.name = name;
		this.pin = pin;
		this.type = type;
		this.timing = timing;
		Collections.sort(this.timing); // 对timing进行排序
		if (type.equals("appliance")) {
			this.gpiopin = GpioFactory.getInstance().provisionDigitalOutputPin(pin);
		}
	}

	public static void Unload() { // 关闭所有定时器, 删除所有注册的gpiopin
		for (Map.Entry<String, Equipment> entry : map.entrySet()) { // 遍历用电器
			Equipment equip = entry.getValue();
			for (int x = 0; x < equip.listtimer.size(); x++) { // 删除原有定时器
				equip.listtimer.get(x).cancel();
			}
			GpioFactory.getInstance().unprovisionPin(equip.gpiopin); // 删除注册的gpiopin
		}
		map.clear(); // 清空map
	}

	public static void Load() {
		List<String> list = yml.getKey(".");
		for (int x = 0; x < list.size(); x++) {
			String key = list.get(x);
			String name = yml.getString(key + "." + "name");
			Pin pin = RaspiPinList.getInstance().NameToPin(yml.getString(key + "." + "pin"));
			String type = yml.getString(key + "." + "type");
			List<String> timing = yml.getList(key + "." + "timing");
			Equipment equip = new Equipment(name, pin, type, timing);
			map.put(key, equip);
			equip.timing();
		}
	}

	public static void Reload() {
		yml.Reload(); // 重载配置文件
		Unload();
		Load();
	}

	public void timing() {
		SimpleDateFormat ft = new SimpleDateFormat("HH:mm:ss");
		Date now = new Date(); // 现在的时间
		String nowstr = new String(ft.format(now));
		int index = timing.size() - 1; // list的最大序数
		for (int x = 0; x < timing.size(); x++) { // 判断现在的时间在timing中所在的位置
			String str = timing.get(x);
			String[] strs = str.split("\\s+"); // 空格分割
			boolean b;
			if (strs[1].equals("on")) {
				b = true;
			} else {
				b = false;
			}
			Date time = Tools.toDate(strs[0]);
			String plan = strs[0]; // 定时计划的时间str
			if (nowstr.compareTo(plan) >= 0) { // 之前的任务
				long day = 86400000;
				time = new Date(time.getTime() + day); // 加24小时
				Inertia(time, b); // 循环定时器创建
				index = x;
			} else { // 之后的任务
				Inertia(time, b); // 循环定时器创建
			}
		}
		if (index >= 0) {
			String str = timing.get(index);
			String[] strs = str.split("\\s+"); // 空格分割
			if (strs[1].equals("on")) {
				((GpioPinDigitalOutput) gpiopin).high();
			} else {
				((GpioPinDigitalOutput) gpiopin).low();
			}
		}
	}

	public void Inertia(Date time, boolean on) { // 在指定日期后循环开关
		Timer timer = new Timer(true);
		TimerTask task = new TimerTask() {
			public void run() {
				if (on) {
					((GpioPinDigitalOutput) gpiopin).high();
					Printer.getInstance().printInfo(Message.EQUIPAUTOOPENED.getMessage() + name);
				} else {
					((GpioPinDigitalOutput) gpiopin).low();
					Printer.getInstance().printInfo(Message.EQUIPAUTOCLOSED.getMessage() + name);
				}
			}
		};
		this.listtimer.add(timer);
		long x = 86400000; // 24小时
		timer.scheduleAtFixedRate(task, time, x); // 理论时间
	}
}
