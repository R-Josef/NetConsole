package moe.feo.NetConsole;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.exception.GpioPinExistsException;

public class Switch {

	public Pin PinOfSwitch;

	public Switch(Pin pin) {
		this.PinOfSwitch = pin;
	}

	public Switch(String pinname) {
		this.PinOfSwitch = RaspiPinList.getInstance().NameToPin(pinname);
	}

	private boolean On_Off(boolean on) { // 开关
		boolean b = false;
		if (PinOfSwitch == null) {
			return b;
		}
		final GpioController controller = GpioFactory.getInstance();
		final GpioPinDigitalOutput pin;
		try {
			pin = controller.provisionDigitalOutputPin(PinOfSwitch);
			PinCache.listpincache.get(PinOfSwitch.getAddress()).setMode(PinMode.DIGITAL_OUTPUT); // 写入缓存
			if (on) {
				pin.high();
				PinCache.listpincache.get(PinOfSwitch.getAddress()).setState(PinState.HIGH);
			} else {
				pin.low();
				PinCache.listpincache.get(PinOfSwitch.getAddress()).setState(PinState.LOW);
			}
			controller.shutdown();
			controller.unprovisionPin(pin);// 必须取消这个pin, 不然会报错
			b = true;
		} catch (GpioPinExistsException e) {
			Printer.getInstance().printWarn(Message.PINEXISTS.getMessage() + e.getPin().getName());
		}
		return b;
	}

	public boolean On() { // 开
		boolean b = false;
		if (On_Off(true)) {
			b = true;
		}
		return b;
	}

	public boolean Off() { // 关
		boolean b = false;
		if (On_Off(false)) {
			b = true;
		}
		return b;
	}

	public void Inertia(Date time, boolean on, boolean loop) { // 在指定日期开关一次或后循环开关
		Timer timer = new Timer(true);
		TimerTask task = new TimerTask() {
			public void run() {
				if (on) {
					Switch sw = new Switch(PinOfSwitch);
					if (sw.On()) {
						Printer.getInstance().printInfo(Message.TIMEROPENED.getMessage()
								+ RaspiPinList.getInstance().getNameByPin(PinOfSwitch));
					}
				} else {
					Switch sw = new Switch(PinOfSwitch);
					if (sw.Off()) {
						Printer.getInstance().printInfo(Message.TIMERCLOSED.getMessage()
								+ RaspiPinList.getInstance().getNameByPin(PinOfSwitch));
					}
				}
			}
		};
		if (!loop) { // 如果不是循环定时
			timer.schedule(task, time);
		} else { // 循环定时
			long x = 86400000; // 24小时
			timer.scheduleAtFixedRate(task, time, x); // 理论时间
		}
	}
}
