package com.qian.word;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qian.word.content.BasicWordController;
import com.qian.word.content.MapListDataSource;

public class WriteWord2 {
	
	
	
	public static void main(String[] args) {
	String doctpl = WriteWord2.class.getResource("wordfile/")+"";//"备选专家名单_tpl.doc";
	doctpl=doctpl.substring(6);

			try {
				BasicWordController bwc=new BasicWordController();
				bwc.openDocument(doctpl, "508174.doc");
				bwc.addImageWatermark(doctpl, "pictures.jpg");
				bwc.toWrite("508174temp.doc");
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	
	public static List getinit(){
		List list=new ArrayList();
		for (int i = 0; i < 10; i++) {
		Map map=new HashMap();
		map.put("personName", "日<"+i+">");
		map.put("sexName", i%2==0?"男":"女");
		map.put("age", i*i/5+2);
		map.put("img", BasicWordController.getBytesFromFile(WriteWord2.class.getResource("wordfile/ani.jpg").toString().substring(5)));
	
		list.add(map);
		}
		return list;
	}
}




