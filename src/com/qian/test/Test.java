package com.qian.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import com.aspose.words.Document;
import com.aspose.words.SaveFormat;

public class Test {
	static String inDir = "E:/word/";
	static String outDir = "e:/pdf/";
	
	
public static boolean jisuanDays(String date1, String date2) { 
		
		Boolean ischao = false;
	    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			java.util.Date date3 = sdf.parse(date1);
			java.util.Date date4 = sdf.parse(date2);
			long shengyu = date4.getTime() - date3.getTime();
			shengyu = shengyu / (1000 * 60 * 60 * 24);
			if(shengyu>=30){
				ischao=true;
			}
		} catch (ParseException e) {
		}
	    return ischao;
    }

    
	public static void main(String[] args)  {
		Test test = new Test();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(Test.jisuanDays("2004-05-01 13:31:40","2004-05-31 13:31:41"));
	}
		
	
	
	

//	public static void main(String[] args) throws Exception {
//		
//	
//	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   
//	    Date d1 = df.parse("2004-05-01 13:31:40");   
//	    Date d2 = df.parse("2004-05-31 13:31:24");   
//	    long diff = d2.getTime() - d1.getTime();   
//	    long days = diff / (1000 * 60 * 60 * 24);   
//	    System.out.println(diff+"时间差值：-----"+days);
//		// Temp t=new Temp();
//		// Document doc= t.execute("test1.docx");
//		// doc.save("t1.docx");
//		//toPDF();
//		// toxls(); 
//		
//		//new Test(). tomove(inDir);
//
//	}

	/**
	 * 移动文件
	 * @param fpath
	 * @throws Exception
	 */
	public  void tomove(String fpath) throws Exception {
		System.out.println(fpath);
		File inFile = new File(fpath);
		if (!inFile.exists()) {
			return;
		}
		String[] files = null;
		if (inFile.isDirectory()) {
			files = inFile.list();
			for (String filestr : files) {
				tomove(fpath +"/"+ filestr);
			}
		
		}
		if (inFile.isFile()) {
			Copy(inFile, outDir+inFile.getName());
		}

	}

	/**
	 * 复制文件
	 * @param oldfile
	 * @param newPath
	 */
	public static void Copy(File oldfile, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			if (oldfile.exists()) {
				InputStream inStream = new FileInputStream(oldfile);
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread;
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
			System.out.println("成功1个"+newPath);
		} catch (Exception e) {
			System.out.println("error  ");
			e.printStackTrace();
		}
	}

	/**
	 * 生成excel
	 * @throws Exception
	 */
	public static void toxls() throws Exception {
		File inFile = new File(inDir);
		List list = new ArrayList();
		if (!inFile.exists()) {
			return;
		}
		String[] files = null;
		if (inFile.isDirectory()) {
			files = inFile.list();
		}

		WritableWorkbook book = Workbook.createWorkbook(new File(" test.xls "));
		// 生成名为“第一页”的工作表，参数0表示这是第一页
		WritableSheet sheet = book.createSheet(" 第一页 ", 0);
		int indexTemp = 0;
		for (int i = 0; i < files.length; i++) {
			File fileTemp = new File(inDir + files[i]);

			if (fileTemp.isDirectory()) {
				String[] fileTemps = fileTemp.list();
				if (fileTemps != null) {
					for (int j = 0; j < fileTemps.length; j++) {
						String s = fileTemps[j];
						list.add(fileTemps[j]);
						System.out.println(s);
						System.out.println(files[i]);
						sheet.addCell(new Label(0, indexTemp, s));
						sheet.addCell(new Label(1, indexTemp, files[i]));
						indexTemp++;

					}
				}

			}
		}

		book.write();
		book.close();
	}

	/**
	 * 转换成pdf
	 * @throws Exception
	 */
	public static void toPDF() throws Exception {

		File inFile = new File(inDir);
		List list = new ArrayList();
		if (!inFile.exists()) {
			return;
		}
		String[] files = null;
		if (inFile.isDirectory()) {
			files = inFile.list();
		}

		int count = 0;
		System.out.println("共有" + files.length + "个文件");

		try {
			if (files != null)
				for (int i = 0; i < files.length; i++) {
					count++;
					Document doc = new Document(inDir + files[i]);
					doc.save(
							outDir
									+ files[i].substring(0,
											files[i].lastIndexOf(".")) + ".pdf",
							SaveFormat.PDF);
					System.out.println("转换进度" + count + "/" + files.length
							+ "个文件");
				}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("转换完成" + files.length + "个文件");
	}

}
