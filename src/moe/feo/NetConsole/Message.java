package moe.feo.NetConsole;

public enum Message {
	WELCOME("welcome"),CLISTARTED("clistarted"),PINOPENED("pinopened"),PINCLOSED("pinclosed"),TIMERCREATED("timercreated"),
	PINCACHESTATE("pincachestate"),PINSTATE("pinstate"),PINMORETHANONE("pinmorethanone"),CANNOTFINDPIN("cannotfindpin"),TIMEROPENED("timeropened"),
	TIMERCLOSED("timerclosed"),INVALIDDATE("invaliddate"),RELOADED("reloaded"),PINEXISTS("pinexists"),EQUIPAUTOOPENED("equipautoopened"),
	EQUIPAUTOCLOSED("equipautoclosed"),EQUIPSTATES("equipstates"),GOODBYE("goodbye"),FAILEDSAVEFILE("failedsavefile"),INVALIDUSAGE("invalidusage");
	
	private String path;
	
	private Message(String path) {
		this.path = path;
	}
	
	private static Config yml = language();
	
	private static Config language() { // 语言检测
		String lang = Tools.config.getString("local");
		Config language = new Config("language/"+lang+".yml");
		return language;
	}
	
	public String getMessage() {
		return yml.getString(path);
	}
	
	public static void Reload() {
		yml = language();
	}
}
