package com.tools;



import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import com.qian.word.content.AutoPrintLog;

/**
 * 打印工具类
 * @author 钱王鹏
 *
 */
public abstract class PrintTools {
	
	
	public static void main(String[] args) {
		PrintTools.printWord("f:\\123.doc","word");
	}

	public static int printWord(String filePath,String log) {
		int stat=0;
		//System.out.println("====================================");
		ActiveXComponent word=null;
		 // 所有word文档集合 
	    Dispatch documents=null; 
	 // word文档 
	    Dispatch doc=null;
		try {
			// word运行程序对象
			word = new ActiveXComponent("Word.Application");

			// visible 为true表示word应用程序可见,为flase表示word应用程序不可见
			word.setProperty("Visible", new Variant(false));

			// 所有word文档集合
			documents = word.getProperty("Documents").toDispatch();

			// 打开一个已存在的文档
			doc = Dispatch.call(documents, "Open",filePath).toDispatch();

			//word.setProperty("ActivePrinter", new Variant("HP LaserJet 4 local on LPT1:"));
			
			// 打印当前文档
			Dispatch.call(doc, "PrintOut");
		} catch (Exception e) {
			AutoPrintLog.write("err : "+log+"打印失败。"+"文件路径为"+filePath+"的打印失败。失败信息:"+e);
			stat=-1;
			e.printStackTrace();
		} finally {
			 if (word != null) { 
                 Dispatch.call(word, "Quit"); 
                 word = null; 
			 } 
			 doc = null; 
			 documents = null; 
		}
		return stat;
	}

}
