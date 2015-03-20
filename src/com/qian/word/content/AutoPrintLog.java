package com.qian.word.content;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
public class AutoPrintLog {

	private static File logfile ;
	private static FileWriter logwriter;
	static{
		
	String logPath ="E:/log.txt";
	logfile = new File(logPath);
	try {
		logwriter = new FileWriter(logfile,true);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		System.out.print("自动打印日志文件读取失败");
	}
	}
	public static synchronized void write(String log){
		try {
			logwriter.write(log+"\r\n");
			logwriter.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
