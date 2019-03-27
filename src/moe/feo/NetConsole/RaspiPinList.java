package moe.feo.NetConsole;

import java.util.List;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

public class RaspiPinList {

	private static RaspiPinList instance = new RaspiPinList();
	private static Config yml = new Config("pinlist.yml");

	private RaspiPinList() {

	}

	public Pin NameToPin(String pinname) { // 将pinlist.yml中用户自己设置的pin名转化为pin
		List<String> KeyList = yml.getKeyOfValue(pinname);
		if (KeyList.size() > 1) {
			Printer.getInstance().printWarn(Message.PINMORETHANONE.getMessage());
		}
		String key = new String();
		try {
			key = KeyList.get(0);
		} catch (IndexOutOfBoundsException e) {
			Printer.getInstance().printWarn(Message.CANNOTFINDPIN.getMessage() + pinname + ".");
			return null;
		}
		String pinnum = key.substring(5, 7); // 取前不取后
		int pinaddr = Integer.parseInt(pinnum);
		return RaspiPin.getPinByAddress(pinaddr);
	}

	public String getNameByPin(Pin pin) { // 将pin转化为pinlist中自定义的pin名
		String key = "GPIO_" + String.format("%02d", pin.getAddress()); // 格式转换
		return yml.getString(key);
	}

	public static RaspiPinList getInstance() {
		return RaspiPinList.instance;
	}

	public void Reload() {
		yml.Reload();
	}
}
