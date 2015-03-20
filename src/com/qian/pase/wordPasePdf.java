package com.qian.pase;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.aspose.words.Document;
import com.aspose.words.SaveFormat;
import com.qian.word.content.BasicWordController;

public class wordPasePdf {

	public static void main(String[] args) {
		BasicWordController bwc= new BasicWordController();
		try {
			Document doc=bwc.openDocument(new FileInputStream("F://1.doc"));
			doc.save("F://1.pdf", SaveFormat.PDF);  
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
