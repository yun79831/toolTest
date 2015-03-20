package com.tools;



import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import com.qian.word.content.AutoPrintLog;

/**
 * ��ӡ������
 * @author Ǯ����
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
		 // ����word�ĵ����� 
	    Dispatch documents=null; 
	 // word�ĵ� 
	    Dispatch doc=null;
		try {
			// word���г������
			word = new ActiveXComponent("Word.Application");

			// visible Ϊtrue��ʾwordӦ�ó���ɼ�,Ϊflase��ʾwordӦ�ó��򲻿ɼ�
			word.setProperty("Visible", new Variant(false));

			// ����word�ĵ�����
			documents = word.getProperty("Documents").toDispatch();

			// ��һ���Ѵ��ڵ��ĵ�
			doc = Dispatch.call(documents, "Open",filePath).toDispatch();

			//word.setProperty("ActivePrinter", new Variant("HP LaserJet 4 local on LPT1:"));
			
			// ��ӡ��ǰ�ĵ�
			Dispatch.call(doc, "PrintOut");
		} catch (Exception e) {
			AutoPrintLog.write("err : "+log+"��ӡʧ�ܡ�"+"�ļ�·��Ϊ"+filePath+"�Ĵ�ӡʧ�ܡ�ʧ����Ϣ:"+e);
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
