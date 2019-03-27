package moe.feo.NetConsole;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

public class Config {
	public Map<String, Object> map = new LinkedHashMap<String, Object>();
	private String path = new String(); // 这个路径指相对jar包的路径, 也可以表示jar包内的根目录(不需要在前面加/)

	public Config(String path) {
		this.Load(path);
		this.path = path;
	}

	@SuppressWarnings("unchecked")
	public void Load(String path) {
		// new FileInputStream(new File(path));
		// .getResource(path); 先搜索包内再搜索包外, 返回的路径中带有jar!不能直接使用,且指的是当前cd工作目录
		// 包成jar文件后, 应当用流的形式读取包内文件
		// .getResourceAsStream(path); 先搜索包内再搜索包外, 返回流
		Yaml yaml = new Yaml();
		InputStream stream = Config.class.getClassLoader().getResourceAsStream(path);
		this.map = (Map<String, Object>) yaml.load(stream);
	}

	public void Save(String path) {

		DumperOptions options = new DumperOptions();
		options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		options.setPrettyFlow(true);
		Yaml yaml = new Yaml(options);

		String jarpath = this.getClass().getClassLoader().getResource("").getPath();
		File file = new File(jarpath + path);
		if (!file.getParentFile().exists()) { // 如果父文件夹不存在
			file.getParentFile().mkdirs(); // 创建父文件夹
		}
		if (file.exists()) { // 如果文件已经存在
			file.delete(); // 删除此文件
		}
		try {
			FileWriter writer = new FileWriter(file);
			yaml.dump(this.map, writer);
		} catch (IOException e) {
			Printer.getInstance().printError(Message.FAILEDSAVEFILE.getMessage());
		}
	}

	public void Save() {
		this.Save(this.path);
	}

	public void Reload() {
		this.Load(this.path);
	}

	@SuppressWarnings("unchecked")
	public Object getValue(String path) { // 从指定路径获取类型
		Object value = new Object(); // 初始化返回的值
		if (path.equals(".")) { // 表示根路径
			value = this.map;
		} else if (path.contains(".")) { // 不止一个key
			String[] keys = path.split("\\.");// 分割路径, "."是转义字符必须加\\
			Map<String, Object> map = new LinkedHashMap<String, Object>(); // 此map用于储存遍历得到的map
			map = (Map<String, Object>) this.map.get(keys[0]); // 直接取第一个父键的值放入内部map
			for (int x = 1; x + 1 <= keys.length - 1; x = x + 1) { // 循环从第二个key开始, 至keys的长度-1为止
				map = (Map<String, Object>) map.get(keys[x]);
			}
			value = map.get(keys[keys.length - 1]); // 直接取最后一个key的值作为value
		} else { // 只有一个key, 直接从map获取值
			value = this.map.get(path);
		}
		return value;
	}

	public String getString(String path) { // 从指定路径获取String
		String value = new String();
		value = this.getValue(path).toString();
		return value;
	}

	@SuppressWarnings("unchecked")
	public List<String> getList(String path) { // 从指定路径获取List
		List<String> value = new ArrayList<String>();
		value = (List<String>) this.getValue(path);
		return value;
	}

	@SuppressWarnings("unchecked")
	public List<String> getKey(String path) { // 从指定路径获取所有子健
		List<String> value = new ArrayList<String>();
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map = (Map<String, Object>) this.getValue(path);
		for (String key : map.keySet()) {
			value.add(key);
		}
		return value;
	}

	public List<String> getKeyOfValue(String value) { // 通过值查找键
		List<String> keyList = new ArrayList<>();
		for (String key : map.keySet()) {
			String strvalue = String.valueOf(map.get(key)); // 将类型(Integer)转化为String
			if (strvalue.equals(value)) {
				keyList.add(key);
			}
		}
		return keyList;
	}

	public boolean getBoolean(String path) {
		boolean value;
		value = (boolean) this.getValue(path);
		return value;
	}
}
