package com.qian.word;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qian.word.content.BasicWordController;
import com.qian.word.content.MapListDataSource;

public class WriteWord {

	/**
	 * ��������ں�ɫ��Ӱ���֣��Ⱥϲ���Ԫ���ٲ��
	 * */
	
	public static void main(String[] args) {
	String doctpl = WriteWord.class.getResource("wordfile/")+"";//"��ѡר������_tpl.doc";
	doctpl=doctpl.substring(6);

			try {
				BasicWordController bwc=new BasicWordController();
				bwc.openDocument(doctpl, "tpl.doc");
				bwc.addImageWatermark(doctpl, "ani.jpg");
				bwc.mergeSingleValue("judgeGroupName", "�Ҹ���һ��");
				bwc.mergeSingleValue("projectCount", "10");
				List<Map<String, Object>> dataList=getinit();
				bwc.mergeBeanList(new MapListDataSource(dataList, "expert"));
				bwc.mergeSingleValue("showa",new FileInputStream(doctpl+"ss.doc"));
				bwc.toWrite("show.doc");
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	
	public static List getinit(){
		List list=new ArrayList();
		for (int i = 0; i < 10; i++) {
		Map map=new HashMap();
		map.put("personName", "s<"+i+">");
		map.put("sexName", i%2==0?"��":"Ů");
		map.put("age", i*i/5+2);
		map.put("img", BasicWordController.getBytesFromFile(WriteWord.class.getResource("wordfile/ani.jpg").toString().substring(5)));
	
		list.add(map);
		}
		return list;
	}
}




