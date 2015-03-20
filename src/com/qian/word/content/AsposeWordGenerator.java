package com.qian.word.content;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URLDecoder;
import java.sql.ResultSet;
import java.util.Map;

import com.aspose.words.Cell;
import com.aspose.words.CellMerge;
import com.aspose.words.DataTable;
import com.aspose.words.Document;
import com.aspose.words.DocumentBuilder;
import com.aspose.words.FontSettings;
import com.aspose.words.IMailMergeDataSource;
import com.aspose.words.License;
import com.aspose.words.Row;
import com.aspose.words.SaveFormat;
import com.aspose.words.Table;

public class AsposeWordGenerator {
	
//	static {
//		try {
//			License license = new License();
//			//license.setLicense(URLDecoder.decode(WordHelper.class.getResource("Aspose.Words.lic").getFile(),"UTF-8"));
//			license.setLicense(AsposeWordGenerator.class.getResourceAsStream("Aspose.Words.lic"));
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new RuntimeException(e.getMessage());
//		}
//	}
	
	static{		
		try {
			License license = new License();
			license.setLicense(URLDecoder.decode(BasicWordController.class.getResource("Aspose.Words.lic").getFile(),"UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
			//throw new RuntimeException(e.getMessage());
		}		
	}
	
	
	public static Document loadTemplet(String realFilePath)throws Exception{
		Document doc = new Document(realFilePath);
		if(System.getProperty("os.name").toLowerCase().indexOf("windows")==-1){
			FontSettings.setFontsFolder("//usr//share//fonts", true);
		}
		return doc;
	}
	
	public static Document mergeArray(Document doc,String[] name,Object[] obj)throws Exception {
		doc.getMailMerge().execute(name, obj);
		return doc;
	}
	
	public static Document mergeSingleValue(Document doc,String name,Object obj) throws Exception{
		return mergeArray(doc,new String[]{name},new Object[]{obj});
	}
	
	public static Document mergeMap(Document doc,Map<String,Object> map) throws Exception{
		String[] param =new String[map.size()];
		Object[] obj =new Object[map.size()];
		int i=0;
		for(Map.Entry<String,Object> entry : map.entrySet()){
			param[i]= entry.getKey();
			obj[i]= entry.getValue();
			i++;
		}
		return mergeArray(doc, param, obj);
	}
	
	public static Document mergeResultSet(Document doc,ResultSet rs) throws Exception{
		doc.getMailMerge().executeWithRegions(new DataTable(rs));
		return doc;
	}	
	

	
	public static Document mergeBeanList(Document doc,IMailMergeDataSource blds) throws Exception{
		doc.getMailMerge().executeWithRegions(blds);
		return doc;
	}
	
	
	public static ByteArrayOutputStream outputStream(Document doc) throws Exception{
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		doc.save(os, SaveFormat.DOC);
		return os;
	}
	
	public static ByteArrayInputStream inputStream(Document doc) throws Exception{
		ByteArrayInputStream is = new ByteArrayInputStream(outputStream(doc).toByteArray());
		return is;
	}
	
	public static Cell getCell(Document doc,String mergeFieldName) throws Exception{
		DocumentBuilder builder = new DocumentBuilder(doc);
		Cell cell = null;
		if(builder.moveToMergeField(mergeFieldName,false,false)){
			if (builder.getCurrentParagraph().getParentNode() instanceof Cell){
				cell = (Cell)builder.getCurrentParagraph().getParentNode();
			}
		}
		if(cell==null){
			System.out.println("文档中没有"+cell+"的格子");
		}
		return cell;			
	}
	
	public static Cell getCell(Table table, int rowIdx, int colIdx){
		return table.getRows().get(rowIdx).getCells().get(colIdx);
	}	
	
	public static Row getRow(Table table, int rowIdx){
		return table.getRows().get(rowIdx);
	}
	
	public static Row getRow(Document doc,String mergeFieldName) throws Exception{
		DocumentBuilder builder = new DocumentBuilder(doc);
		Row row = null;
		if(builder.moveToMergeField(mergeFieldName,false,false)){
			if (builder.getCurrentParagraph().getParentNode() instanceof Cell){
				Cell cell = (Cell)builder.getCurrentParagraph().getParentNode();
				row = (Row)cell.getParentNode();
			}
		}
		if(row==null){
			System.out.println("文档中没有"+row+"的行");
		}
		return row;		
	}
	
	public static Table getTable(Document doc,String mergeFieldName) throws Exception{
		DocumentBuilder builder = new DocumentBuilder(doc);
		Table table = null;
		if(builder.moveToMergeField(mergeFieldName,false,false)){
			if (builder.getCurrentParagraph().getParentNode() instanceof Cell){
				Cell cell = (Cell)builder.getCurrentParagraph().getParentNode();
				Row row = (Row)cell.getParentNode();
				table = row.getParentTable();
			}
		}
		if(table==null){
			System.out.println("文档中没有"+mergeFieldName+"的表格");
		}
		return table;		
	}
	
	public static void mergeCell(Cell cell,int horizontalMergeCount,int verticalMergeCount) throws Exception{
		if(horizontalMergeCount<0) throw new Exception("水平合并的单元格个数不能小于0！");
		if(verticalMergeCount<0) throw new Exception("垂直合并的单元格个数不能小于0！");
		if(horizontalMergeCount+verticalMergeCount>0){
			if(horizontalMergeCount>0) cell.getCellFormat().setHorizontalMerge(CellMerge.FIRST);
			if(verticalMergeCount>0) cell.getCellFormat().setVerticalMerge(CellMerge.FIRST);
			
			Row row = cell.getParentRow();
			Table table = row.getParentTable();
			int rowNo = row.getParentTable().getRows().indexOf(row);
			int colNo = row.getCells().indexOf(cell);
			
			int rowCount = table.getRows().getCount();
			int hxhs = 0; //垂直合并时行循环的实际个数
			int lxhs = 0;//水平合并时列循环的实际个数
			if(horizontalMergeCount>0&&verticalMergeCount==0){
				lxhs = row.getCount()-colNo-horizontalMergeCount>0?horizontalMergeCount:row.getCount()-colNo;
				for(int j=colNo+1;j<colNo+lxhs;j++){
					Cell c = row.getCells().get(j);
					c.getCellFormat().setHorizontalMerge(CellMerge.PREVIOUS);
				}
			}
			if(horizontalMergeCount==0&&verticalMergeCount>0){
				hxhs = rowCount-rowNo>verticalMergeCount?verticalMergeCount:rowCount-rowNo;
				for(int i=rowNo+1;i<rowNo+hxhs;i++){
					Row r = table.getRows().get(i);
					Cell c = r.getCells().get(colNo);
					c.getCellFormat().setVerticalMerge(CellMerge.PREVIOUS);
				}
			}
			
			if(horizontalMergeCount>0&&verticalMergeCount>0){		
				hxhs = rowCount-rowNo>verticalMergeCount?verticalMergeCount:rowCount-rowNo;
				for(int i=rowNo;i<rowNo+hxhs;i++){
					Row r = table.getRows().get(i);
					lxhs = r.getCount()-colNo-horizontalMergeCount>0?horizontalMergeCount:r.getCount()-colNo;
					for(int j=colNo;j<colNo+lxhs;j++){
						Cell c = r.getCells().get(j);
						if(i!=rowNo||j!=colNo){
							c.getCellFormat().setHorizontalMerge(CellMerge.PREVIOUS);
						}
					}
				}
				for(int i=rowNo+1;i<rowNo+hxhs;i++){
					Row r = table.getRows().get(i);
					Cell c = r.getCells().get(colNo);
					c.getCellFormat().setVerticalMerge(CellMerge.PREVIOUS);
				}
			}		
		}
	}
	
	public static void main(String...strings) throws Exception{
		Document doc = AsposeWordGenerator.loadTemplet("D:\\work\\zhuan_jia_lin_xuan\\template\\test_tpl.doc");
		getTable(doc, "subjectName");
	}

}
