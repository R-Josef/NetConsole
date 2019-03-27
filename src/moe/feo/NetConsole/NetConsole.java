package moe.feo.NetConsole;

public class NetConsole {

	public static void main(String[] args) {
		System.out.println(Message.WELCOME.getMessage());
		if (Tools.config.getBoolean("initialize.install")) {
			Tools.Initialize(); // 初始化引脚
		}
		Equipment.Load();
		CLI.getInstance().startTheard();
	}

}