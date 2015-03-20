package com.qian.jxl;

import java.io.File;
import java.io.IOException;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class jxlTest {

	public static void main(String[] args) {
		try {
			// ByteArrayOutputStream bos=new ByteArrayOutputStream();
			// WritableWorkbook wwb = Workbook.createWorkbook(bos);
			WritableWorkbook wwb = Workbook.createWorkbook(new File(
					"E:/text.xls"));//创建exl
			WritableSheet ws = wwb.createSheet("汇总表", 0);
			ws.mergeCells(0, 0,1, 0);//合并单元格 横向合并   
			ws.mergeCells(2, 0, 3, 0);

			ws.setRowView(0, 800);
			WritableFont font1 = new WritableFont(
					WritableFont.createFont("宋体"), 15, WritableFont.NO_BOLD);
			WritableCellFormat h1cf = new WritableCellFormat(font1);
			h1cf.setAlignment(Alignment.LEFT);
			h1cf.setVerticalAlignment(VerticalAlignment.CENTRE);
			ws.addCell(new Label(0, 0, "评审组：", h1cf));
			ws.addCell(new Label(2, 0, "评审组1：", h1cf));
			WritableFont font2 = new WritableFont(
					WritableFont.createFont("宋体"), 11, WritableFont.NO_BOLD);
			WritableCellFormat h2cf = new WritableCellFormat(font2);
			h2cf.setAlignment(Alignment.CENTRE);
			h2cf.setVerticalAlignment(VerticalAlignment.CENTRE);
			ws.mergeCells(0, 1, 0, 2);
			ws.mergeCells(1, 1, 1, 2);
			ws.mergeCells(2, 1, 2, 2);
			ws.mergeCells(3, 1, 3, 2);
			ws.setColumnView(1, 800);
			ws.setColumnView(2, 800);
			ws.addCell(new Label(0, 1, "序号", h2cf));
			ws.addCell(new Label(1, 1, "项目名称", h2cf));
			ws.addCell(new Label(2, 1, "第一完成单位", h2cf));
			ws.addCell(new Label(3, 1, "数量", h2cf));
			wwb.write();
			wwb.close();
			// new ByteArrayInputStream(bos.toByteArray())
		} catch (RowsExceededException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
