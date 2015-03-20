package com.qian.test;

import java.io.File;

import jxl.*;

public class jxlTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		 try {
	            Workbook book = Workbook.getWorkbook(new File("e:/测试.xls"));
	            // 获得第一个工作表对象
	            Sheet sheet = book.getSheet(0);
	            // 得到单元格
	            for (int i = 0; i < sheet.getColumns(); i++) {
	            	if(!sheet.getCell(i, 0).getContents().equals("你想要读的列") )
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


