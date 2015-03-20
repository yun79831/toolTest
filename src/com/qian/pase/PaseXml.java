package com.qian.pase;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
/**
 * 提取特定目录下xml配置
 * @author admin
 *
 */
public class PaseXml {
	static Map map = new HashMap<String, Object>();
	private static final String CONFIG_FILE_DIR = "config/codemapping";// 配置文件所在目录
	static {
		initialize();
	}

	/**
	 * 测试
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		String fileName = "fileupload.global.temporaryDir";
		String value = getValue(fileName);
		System.out.println(value);
	}

	/**
	 * 初始化加载配置文件
	 */
	private static void initialize() {
		// 加载路径
		String findpath = ClassLoader.getSystemResource("").toString();
		if (findpath.indexOf("file:/") == 0)
			findpath = ClassLoader.getSystemResource("").toString()
					.substring(6);
		String fileDir = findpath + "//" + CONFIG_FILE_DIR;
		// 指定目录不存在
		File dir = new File(fileDir);
		if (!dir.exists()) {
			System.out.println("指定的目录不存在：" + dir.getAbsolutePath());
			return;
		}
		// 获取目录下的配置文件列表
		File[] files = dir.listFiles(new FilenameFilter() {
			public boolean accept(File file, String name) {
				return name.endsWith(".xml");
			}
		});

		for (int i = 0; i < files.length; i++) {
			map.put(files[i].getName().substring(0,
					files[i].getName().lastIndexOf('.')), files[i].toString());
		}
	}

	/**
	 * 获取xml中指定的value值
	 * @param fileName
	 *       xml的名字+id+key值
	 */
	public static String getValue(String fileName) {
		String returnstr = null;
		String[] files = fileName.split("\\.");

		SAXReader saxReader = new SAXReader();
		try {
			Document document = saxReader.read(new File(map.get(files[0])
					.toString()));
			Element root = document.getRootElement();
			List constants = root.elements("constant");
			for (int i = 0; i < constants.size(); i++) {
				Element constant = (Element) constants.get(i);
				if (files[1].equals(constant.attributeValue("id"))) {
					List items = constant.elements("item");
					for (int j = 0; j < items.size(); j++) {
						Element item = (Element) items.get(j);
						if (files[2].equals(item.attributeValue("key"))) {
							returnstr = item.attributeValue("value");
							break;
						}

					}
					break;
				}
			}

		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return returnstr;
	}

}
