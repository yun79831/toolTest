package com.qian.test;

import java.io.File;

import jxl.*;

public class jxlTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		 try {
	            Workbook book = Workbook.getWorkbook(new File("e:/����.xls"));
	            // ��õ�һ�����������
	            Sheet sheet = book.getSheet(0);
	            // �õ���Ԫ��
	            for (int i = 0; i < sheet.getColumns(); i++) {
	            	if(!sheet.getCell(i, 0).getContents().equals("����Ҫ������") )
	            		continue;
	                for (int j = 0; j < sheet.getRows(); j++) {
	                    Cell cell = sheet.getCell(i, j);
	                    System.out.print(cell.getContents() + "  ");
	                }
	                System.out.println();
	            }
	            book.close();
	        } catch (Exception e) {
	            System.out.println(e);
	        }
	    }
	}


