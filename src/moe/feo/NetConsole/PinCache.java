package moe.feo.NetConsole;

import java.util.ArrayList;
import java.util.List;

import com.pi4j.io.gpio.GpioProviderPinCache;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

public class PinCache {
	public static final GpioProviderPinCache gpio0 = new GpioProviderPinCache(RaspiPin.GPIO_00);
	public static final GpioProviderPinCache gpio1 = new GpioProviderPinCache(RaspiPin.GPIO_01);
	public static final GpioProviderPinCache gpio2 = new GpioProviderPinCache(RaspiPin.GPIO_02);
	public static final GpioProviderPinCache gpio3 = new GpioProviderPinCache(RaspiPin.GPIO_03);
	public static final GpioProviderPinCache gpio4 = new GpioProviderPinCache(RaspiPin.GPIO_04);
	public static final GpioProviderPinCache gpio5 = new GpioProviderPinCache(RaspiPin.GPIO_05);
	public static final GpioProviderPinCache gpio6 = new GpioProviderPinCache(RaspiPin.GPIO_06);
	public static final GpioProviderPinCache gpio7 = new GpioProviderPinCache(RaspiPin.GPIO_07);
	public static final GpioProviderPinCache gpio8 = new GpioProviderPinCache(RaspiPin.GPIO_08);
	public static final GpioProviderPinCache gpio9 = new GpioProviderPinCache(RaspiPin.GPIO_09);
	public static final GpioProviderPinCache gpio10 = new GpioProviderPinCache(RaspiPin.GPIO_10);
	public static final GpioProviderPinCache gpio11 = new GpioProviderPinCache(RaspiPin.GPIO_11);
	public static final GpioProviderPinCache gpio12 = new GpioProviderPinCache(RaspiPin.GPIO_12);
	public static final GpioProviderPinCache gpio13 = new GpioProviderPinCache(RaspiPin.GPIO_13);
	public static final GpioProviderPinCache gpio14 = new GpioProviderPinCache(RaspiPin.GPIO_14);
	public static final GpioProviderPinCache gpio15 = new GpioProviderPinCache(RaspiPin.GPIO_15);
	public static final GpioProviderPinCache gpio16 = new GpioProviderPinCache(RaspiPin.GPIO_16);
	public static final GpioProviderPinCache gpio17 = new GpioProviderPinCache(RaspiPin.GPIO_17);
	public static final GpioProviderPinCache gpio18 = new GpioProviderPinCache(RaspiPin.GPIO_18);
	public static final GpioProviderPinCache gpio19 = new GpioProviderPinCache(RaspiPin.GPIO_19);
	public static final GpioProviderPinCache gpio20 = new GpioProviderPinCache(RaspiPin.GPIO_20);
	public static final GpioProviderPinCache gpio21 = new GpioProviderPinCache(RaspiPin.GPIO_21);
	public static final GpioProviderPinCache gpio22 = new GpioProviderPinCache(RaspiPin.GPIO_22);
	public static final GpioProviderPinCache gpio23 = new GpioProviderPinCache(RaspiPin.GPIO_23);
	public static final GpioProviderPinCache gpio24 = new GpioProviderPinCache(RaspiPin.GPIO_24);
	public static final GpioProviderPinCache gpio25 = new GpioProviderPinCache(RaspiPin.GPIO_25);
	public static final GpioProviderPinCache gpio26 = new GpioProviderPinCache(RaspiPin.GPIO_26);
	public static final GpioProviderPinCache gpio27 = new GpioProviderPinCache(RaspiPin.GPIO_27);
	public static final GpioProviderPinCache gpio28 = new GpioProviderPinCache(RaspiPin.GPIO_28);
	public static final GpioProviderPinCache gpio29 = new GpioProviderPinCache(RaspiPin.GPIO_29);
	public static final GpioProviderPinCache gpio30 = new GpioProviderPinCache(RaspiPin.GPIO_30);
	public static final GpioProviderPinCache gpio31 = new GpioProviderPinCache(RaspiPin.GPIO_31);
	public static final List<GpioProviderPinCache> listpincache = new ArrayList<GpioProviderPinCache>();
	static { // 序号即gpio编号
		listpincache.add(0, gpio0);
		listpincache.add(1, gpio1);
		listpincache.add(2, gpio2);
		listpincache.add(3, gpio3);
		listpincache.add(4, gpio4);
		listpincache.add(5, gpio5);
		listpincache.add(6, gpio6);
		listpincache.add(7, gpio7);
		listpincache.add(8, gpio8);
		listpincache.add(9, gpio9);
		listpincache.add(10, gpio10);
		listpincache.add(11, gpio11);
		listpincache.add(12, gpio12);
		listpincache.add(13, gpio13);
		listpincache.add(14, gpio14);
		listpincache.add(15, gpio15);
		listpincache.add(16, gpio16);
		listpincache.add(17, gpio17);
		listpincache.add(18, gpio18);
		listpincache.add(19, gpio19);
		listpincache.add(20, gpio20);
		listpincache.add(21, gpio21);
		listpincache.add(22, gpio22);
		listpincache.add(23, gpio23);
		listpincache.add(24, gpio24);
		listpincache.add(25, gpio25);
		listpincache.add(26, gpio26);
		listpincache.add(27, gpio27);
		listpincache.add(28, gpio28);
		listpincache.add(29, gpio29);
		listpincache.add(30, gpio30);
		listpincache.add(31, gpio31);
	}

	public static Pin getPinByCache(GpioProviderPinCache pincache) { // 通过缓存找pin
		try {
			return RaspiPin.getPinByAddress(listpincache.indexOf(pincache));
		} catch (NullPointerException e) {
			return null;
		}
	}

}
