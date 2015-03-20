package com.qian.word.content;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.sql.ResultSet;
import java.util.Map;

import com.aspose.words.BreakType;
import com.aspose.words.Cell;
import com.aspose.words.CellMerge;
import com.aspose.words.CellVerticalAlignment;
import com.aspose.words.DataTable;
import com.aspose.words.Document;
import com.aspose.words.DocumentBuilder;
import com.aspose.words.FontSettings;
import com.aspose.words.HeaderFooterType;
import com.aspose.words.HorizontalAlignment;
import com.aspose.words.License;
import com.aspose.words.Node;
import com.aspose.words.NodeType;
import com.aspose.words.Paragraph;
import com.aspose.words.ParagraphAlignment;
import com.aspose.words.ProtectionType;
import com.aspose.words.RelativeHorizontalPosition;
import com.aspose.words.RelativeVerticalPosition;
import com.aspose.words.Row;
import com.aspose.words.SaveFormat;
import com.aspose.words.Section;
import com.aspose.words.Shape;
import com.aspose.words.Table;
import com.aspose.words.VerticalAlignment;
import com.aspose.words.WrapType;

/**
 * 基本的word控制器修改版
 * 
 * @author 钱王鹏
 * 
 */
public class BasicWordController {
	private Document doc = null;

	/**
	 * 添加Aspose.Word许可认证文件
	 */
	static {
		try {
			License license = new License();
			license.setLicense(URLDecoder.decode(BasicWordController.class
					.getResource("Aspose.Words.lic").getFile(), "UTF-8"));

			if (System.getProperty("os.name").toLowerCase().indexOf("windows") == -1
					&& System.getProperty("os.name").toLowerCase()
							.indexOf("mac") == -1) {// 在linux下加载字体库
				FontSettings.setFontsFolder("//usr//share//fonts", true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 打开一个新Word文档
	 * 
	 * @throws Exception
	 */
	public Document openNewDocument() throws Exception {
		doc = new Document();
		return doc;
	}

	/**
	 * 打开Word文档
	 * 
	 * @param docPath
	 *            文档打开路径
	 * @param fileName
	 *            　文档名称
	 * @throws Exception
	 */
	public Document openDocument(String docPath, String fileName)
			throws Exception {
		doc = new Document(docPath + fileName);
		return doc;
	}

	/**
	 * 打开Word文档
	 * 
	 * @param stream
	 *            文件流
	 * @throws Exception
	 */
	public Document openDocument(InputStream stream) throws Exception {
		doc = new Document(stream);
		return doc;
	}

	/**
	 * 获得当前控制的文档
	 * 
	 * @throws Exception
	 */
	public Document getCurrentDocument() throws Exception {
		return doc;
	}

	/**
	 * 获得当前控制的文档
	 * 
	 * @throws Exception
	 */
	public void setCurrentDocument(Document doc) throws Exception {
		this.doc = doc;
	}

	/**
	 * 保存文档
	 * 
	 * @param docPath
	 *            　文档保存路径
	 * @param fileName
	 *            文档名称
	 * @param saveFormat
	 *            文档保存的格式。具体信息参见com.mrg.kwmis.word.constant.SaveFormat类
	 * @throws Exception
	 */
	public void saveDocument(String docPath, String fileName, int saveFormat)
			throws Exception {
		doc.save(docPath + fileName, saveFormat);
	}

	/**
	 * 保存文档
	 * 
	 * @param outputStream
	 *            　保存文档的输出流
	 * @param saveFormat
	 *            文档保存的格式。具体信息参见com.mrg.kwmis.word.constant.SaveFormat类
	 * @throws Exception
	 */
	public void saveDocument(java.io.OutputStream outputStream, int saveFormat)
			throws Exception {
		doc.save(outputStream, saveFormat);
	}

	/**
	 * 文档保护
	 * 
	 * @param password
	 *            密码
	 * @throws Exception
	 */
	public void protectDocument(String password) throws Exception {
		doc.protect(ProtectionType.ALLOW_ONLY_FORM_FIELDS, password);
	}

	// -------------------------------文件和流的操作-----------------------------//
	/**
	 * 返回输出流
	 * 
	 * @return
	 * @throws Exception
	 */
	public ByteArrayOutputStream outputStream() {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			doc.save(os, SaveFormat.DOC);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return os;
	}

	/**
	 * 返回输出流
	 * 
	 * @return
	 * @throws Exception
	 */
	public ByteArrayInputStream inputStream() {
		ByteArrayInputStream is = new ByteArrayInputStream(outputStream()
				.toByteArray());
		return is;
	}

	/***
	 * 文件转化为二进制
	 * 
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public static byte[] getBytesFromFile(String filePath) {
		byte[] image = null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(filePath);
			image = new byte[fis.available()];
			fis.read(image);
			fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fis != null)
					fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return image;
	}

	public void toWrite(String filePath) {
		InputStream in =null;
		OutputStream os =null;
		try {
			 in = inputStream();
			 os = new FileOutputStream(new File(filePath));
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = in.read(buffer, 0, 8192)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				os.close();
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	// ****************************插入域的操作*******************************//
	/**
	 * 插入循环域
	 * 
	 * @param doc
	 * @param blds
	 * @return
	 * @throws Exception
	 */
	public Document mergeBeanList(MapListDataSource blds) throws Exception {
		doc.getMailMerge().executeWithRegions(blds);
		return doc;
	}

	/**
	 * 
	 * 域添加内容--基本方法
	 * 
	 * @param name
	 * @param obj
	 * @throws Exception
	 */
	public void mergeArray(String[] name, Object[] obj) throws Exception {
		doc.getMailMerge().execute(name, obj);
	}

	/***
	 * 简化后的添加单个
	 * 
	 * @param name
	 * @param obj
	 * @throws Exception
	 */
	public void mergeSingleValue(String name, Object obj) throws Exception {
		mergeArray(new String[] { name }, new Object[] { obj });
	}

	/**
	 * 简化后的添加多个
	 * 
	 * @param map
	 * @throws Exception
	 */
	public void mergeMap(Map<String, Object> map) throws Exception {
		String[] param = new String[map.size()];
		Object[] obj = new Object[map.size()];
		int i = 0;
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			param[i] = entry.getKey();
			obj[i] = entry.getValue();
			i++;
		}
		mergeArray(param, obj);
	}

	public void mergeResultSet(ResultSet rs) throws Exception {
		doc.getMailMerge().executeWithRegions(new DataTable(rs));

	}

	/**
	 * 获得一个表格对象
	 * @param tableIndex
	 *            表格在文档中的顺序号（以0开始计数）
	 * @return
	 */
	public Table getTableByIndex(int tableIndex) throws Exception {
		return doc.getFirstSection().getBody().getTables().get(tableIndex);
	}

	/**
	 * 根据邮件域名称，获取该域下的一个节点
	 * 
	 * @param nodeName
	 * @return
	 * @throws Exception
	 */
	public Node getNodeByName(String nodeName) throws Exception {
		DocumentBuilder builder = new DocumentBuilder(doc);
		Node node = null;
		if (builder.moveToMergeField(nodeName, false, false)) {
			node = builder.getCurrentParagraph().getNextSibling();
		}
		return node;
	}

	/**
	 * 获得一个表格对象
	 * 
	 * @param tableName
	 *            表名
	 * @return
	 * @throws Exception
	 */
	public Table getTableByName(String tableName) throws Exception {
		DocumentBuilder builder = new DocumentBuilder(doc);
		Table table = null;
		if (builder.moveToMergeField(tableName, false, false)) {
			table = (Table) builder.getCurrentParagraph().getNextSibling();
		}
		if (table == null) {
			// System.out.println("文档中没有"+tableName+"的表格");
		}
		return table;

	}

	public Section getSectionByName(String nodeName) throws Exception {
		DocumentBuilder builder = new DocumentBuilder(doc);
		Section node = null;
		if (builder.moveToMergeField(nodeName, false, false)) {
			node = builder.getCurrentSection();
		}
		if (node == null) {
			throw new Exception("文档中没有" + nodeName + "的段落");
		} else {
			return node;
		}
	}

	/**
	 * 获得表格中的指定行
	 * 
	 * @param table
	 *            表格在文档中的顺序号（以0开始计数）
	 * @param rowIndex
	 *            行序号（以0开始计数）
	 * @return
	 */
	public Row getRow(Table table, int rowIndex) throws Exception {
		Row row = null;
		if (table != null) {
			row = table.getRows().get(rowIndex);
		}
		return row;
	}

	/**
	 * 获得表格中的指定行
	 * 
	 * @param tableIndex
	 *            表格在文档中的顺序号（以0开始计数）
	 * @param rowIndex
	 *            行序号（以0开始计数）
	 * @return
	 */
	public Row getRow(int tableIndex, int rowIndex) throws Exception {
		return getRow(getTableByIndex(tableIndex), rowIndex);
	}

	/**
	 * 获得表格中的指定行
	 * 
	 * @param tableName
	 *            表格名称
	 * @param rowIndex
	 *            行序号
	 * @return
	 */
	public Row getRow(String tableName, int rowIndex) throws Exception {
		return getRow(getTableByName(tableName), rowIndex);
	}

	/**
	 * 获得行中的指定列
	 * 
	 * @param table
	 *            表格在文档中的顺序号（以0开始计数）
	 * @param rowIndex
	 *            行序号（以0开始计数）
	 * @param cellIndex
	 *            列序号（以0开始计数）
	 * @return
	 * @throws Exception
	 */
	public Cell getCell(Table table, int rowIndex, int cellIndex)
			throws Exception {
		return getCell(getRow(table, rowIndex), cellIndex);
	}

	/**
	 * 获得行中的指定列
	 * 
	 * @param row
	 *            行对象
	 * @param columnIndex
	 *            列序号（以0开始计数）
	 * @return
	 */
	public Cell getCell(Row row, int cellIndex) throws Exception {
		Cell cell = null;
		if (row != null) {
			cell = row.getCells().get(cellIndex);
		}
		return cell;
	}

	/**
	 * 获得行中的指定列
	 * 
	 * @param tableIndex
	 *            表格在文档中的顺序号（以0开始计数）
	 * @param rowIndex
	 *            行序号（以0开始计数）
	 * @param columnIndex
	 *            列序号（以0开始计数）
	 * @return
	 */
	public Cell getCell(int tableIndex, int rowIndex, int cellIndex)
			throws Exception {
		return getRow(getTableByIndex(tableIndex), rowIndex).getCells().get(
				cellIndex);
	}

	/**
	 * 获得行中的指定列
	 * 
	 * @param tableName
	 *            表格名称
	 * @param rowIndex
	 *            行序号（以0开始计数）
	 * @param columnIndex
	 *            列序号（以0开始计数）
	 * @return
	 */
	public Cell getCell(String tableName, int rowIndex, int cellIndex)
			throws Exception {
		return getCell(getRow(getTableByName(tableName), rowIndex), cellIndex);
	}

	/**
	 * 拆分单元格
	 * 
	 * @param cell
	 *            要拆分的单元格对象
	 * @param splitCount
	 *            拆分个数
	 * @return
	 */
	public void splitCell(Cell cell, int splitCount, String fillStr)
			throws Exception {
		if (cell != null) {
			if (fillStr == null || "".equals(fillStr)) {
				fillStr = "cell";
			}
			DocumentBuilder builder = new DocumentBuilder(doc);
			double cellWidth = cell.getCellFormat().getWidth() / splitCount;
			Row row = cell.getParentRow();
			int rowNo = row.getParentTable().getRows().indexOf(row);
			int colNo = row.getCells().indexOf(cell);
			cell.remove();
			for (int n = 0; n < splitCount; n++) {
				Cell newCell = new Cell(doc);
				newCell.getCellFormat().setWidth(cellWidth);
				newCell.getCellFormat().setVerticalAlignment(
						CellVerticalAlignment.CENTER);
				row.appendChild(newCell);
				Paragraph p = new Paragraph(doc);
				p.getParagraphFormat().setAlignment(ParagraphAlignment.CENTER);
				newCell.appendChild(p);
				builder.moveTo(p);
				builder.insertField("MERGEFIELD " + fillStr + "_" + rowNo + "_"
						+ (colNo + n) + " \\* MERGEFORMAT", "");
			}
		}
	}

	/**
	 * 合并单元格
	 * 
	 * @param cell
	 *            合并范围中的开始单元格对象
	 * @param horizontalMergeCount
	 *            水平合并的单元格个数(不合并给0)
	 * @param verticalMergeCount
	 *            垂直合并的单元格个数(不合并给0)
	 * @throws Exception
	 */
	public void mergeCell(Cell cell, int horizontalMergeCount,
			int verticalMergeCount) throws Exception {
		if (horizontalMergeCount < 0)
			throw new Exception("水平合并的单元格个数不能小于0！");
		if (verticalMergeCount < 0)
			throw new Exception("垂直合并的单元格个数不能小于0！");
		if (horizontalMergeCount + verticalMergeCount > 0) {
			if (horizontalMergeCount > 0)
				cell.getCellFormat().setHorizontalMerge(CellMerge.FIRST);
			if (verticalMergeCount > 0)
				cell.getCellFormat().setVerticalMerge(CellMerge.FIRST);

			Row row = cell.getParentRow();
			Table table = row.getParentTable();
			int rowNo = row.getParentTable().getRows().indexOf(row);
			int colNo = row.getCells().indexOf(cell);

			int rowCount = table.getRows().getCount();
			int hxhs = 0; // 垂直合并时行循环的实际个数
			int lxhs = 0;// 水平合并时列循环的实际个数
			if (horizontalMergeCount > 0 && verticalMergeCount == 0) {
				lxhs = row.getCount() - colNo - horizontalMergeCount > 0 ? horizontalMergeCount
						: row.getCount() - colNo;
				for (int j = colNo + 1; j < colNo + lxhs; j++) {
					Cell c = row.getCells().get(j);
					c.getCellFormat().setHorizontalMerge(CellMerge.PREVIOUS);
				}
			}
			if (horizontalMergeCount == 0 && verticalMergeCount > 0) {
				hxhs = rowCount - rowNo > verticalMergeCount ? verticalMergeCount
						: rowCount - rowNo;
				for (int i = rowNo + 1; i < rowNo + hxhs; i++) {
					Row r = table.getRows().get(i);
					Cell c = r.getCells().get(colNo);
					c.getCellFormat().setVerticalMerge(CellMerge.PREVIOUS);
				}
			}

			if (horizontalMergeCount > 0 && verticalMergeCount > 0) {
				hxhs = rowCount - rowNo > verticalMergeCount ? verticalMergeCount
						: rowCount - rowNo;
				for (int i = rowNo; i < rowNo + hxhs; i++) {
					Row r = table.getRows().get(i);
					lxhs = r.getCount() - colNo - horizontalMergeCount > 0 ? horizontalMergeCount
							: r.getCount() - colNo;
					for (int j = colNo; j < colNo + lxhs; j++) {
						Cell c = r.getCells().get(j);
						if (i != rowNo || j != colNo) {
							c.getCellFormat().setHorizontalMerge(
									CellMerge.PREVIOUS);
						}
					}
				}
				for (int i = rowNo + 1; i < rowNo + hxhs; i++) {
					Row r = table.getRows().get(i);
					Cell c = r.getCells().get(colNo);
					c.getCellFormat().setVerticalMerge(CellMerge.PREVIOUS);
				}
			}
		}
	}

	/**
	 * 添加图片水印
	 * 
	 * @param imageFilePath
	 *            水印图片路径
	 * @param imageFilePath
	 *            水印图片名称
	 * @throws Exception
	 */
	public void addImageWatermark(String imageFilePath, String imageFileName)
			throws Exception {
		DocumentBuilder builder = new DocumentBuilder(doc);
		builder.moveToHeaderFooter(HeaderFooterType.HEADER_PRIMARY);
		Shape shape = builder.insertImage(imageFilePath + imageFileName);
		shape.setWrapType(WrapType.NONE);
		shape.setBehindText(true);
		shape.setZOrder(5);
		shape.setAllowOverlap(true);
		shape.setRelativeHorizontalPosition(RelativeHorizontalPosition.PAGE);
		shape.setHorizontalAlignment(HorizontalAlignment.CENTER);
		shape.setRelativeVerticalPosition(RelativeVerticalPosition.PAGE);
		shape.setVerticalAlignment(VerticalAlignment.CENTER);
		builder.moveToHeaderFooter(HeaderFooterType.FOOTER_PRIMARY);
	}

	/**
	 * 添加一张图片
	 * 
	 * @param imageFilePath
	 *            图片路径
	 * @param imageFilePath
	 *            图片名称
	 * @throws Exception
	 */
	public void addImage(String imageFilePath, String imageFileName)
			throws Exception {

		DocumentBuilder builder = new DocumentBuilder(doc);
		builder.moveToDocumentEnd();// 到文档结尾
		builder.insertBreak(BreakType.PAGE_BREAK);// 插入个分页符

		Shape shape = builder.insertImage(imageFilePath + imageFileName);
		shape.setWrapType(WrapType.NONE);// 使图像浮动
		shape.setBehindText(true);// 是否低于或高于文字

		// 当要显示的图片的宽度大于文档的宽度时，将图片宽度设置成文档的宽度-10
		if (shape.getWidth() > builder.getPageSetup().getPageWidth()) {
			shape.setWidth(builder.getPageSetup().getPageWidth() - 10);
		}

		// 当要显示的图片的高度大于文档的高度时，将图片高度设置成文档的高度-10
		if (shape.getHeight() > builder.getPageSetup().getPageHeight()) {
			shape.setHeight(builder.getPageSetup().getPageHeight() - 10);
		}

		// 指定水平位置和垂直位置
		shape.setRelativeHorizontalPosition(RelativeHorizontalPosition.PAGE);
		shape.setHorizontalAlignment(HorizontalAlignment.CENTER);
		shape.setRelativeVerticalPosition(RelativeVerticalPosition.PAGE);
		shape.setVerticalAlignment(VerticalAlignment.CENTER);

	}

	/**
	 * 添加PDF文件
	 * 
	 * @param filePath
	 *            文件路径
	 * @param fileName
	 *            文件名称
	 * @throws Exception
	 */
	public void addPDF(String pdfFilePath, String pdfFileName) throws Exception {
		DocumentBuilder builder = new DocumentBuilder(doc);
		builder.moveToHeaderFooter(HeaderFooterType.HEADER_PRIMARY);
		Shape shape = builder.insertImage(pdfFilePath + pdfFileName);
		shape.setWrapType(WrapType.NONE);
		shape.setBehindText(true);
		shape.setZOrder(5);
		shape.setAllowOverlap(true);
		shape.setRelativeHorizontalPosition(RelativeHorizontalPosition.PAGE);
		shape.setHorizontalAlignment(HorizontalAlignment.CENTER);
		shape.setRelativeVerticalPosition(RelativeVerticalPosition.PAGE);
		shape.setVerticalAlignment(VerticalAlignment.CENTER);
		builder.moveToHeaderFooter(HeaderFooterType.FOOTER_PRIMARY);
	}

	/**
	 * 填充标签所对应的值
	 * 
	 * @param list
	 *            LabelValueBean对象集合
	 * @throws Exception
	 */
	public void fillMergeField(ResultSet rs) throws Exception {
		// 李志杰于2011-6-17日修改此处
		// doc.getMailMerge().execute(rs);
		doc.getMailMerge().execute(new DataTable(rs));
	}

	/**
	 * 填充邮件合并（表）域的域值
	 * 
	 * @param tableName
	 *            表名
	 * @param rs
	 *            ResultSet对象
	 * @throws Exception
	 */
	public void fillMergeField(String tableName, ResultSet rs) throws Exception {
		// doc.getMailMerge().executeWithRegions(tableName, rs);
		// 李志杰于2011-6-17日修改此处
		doc.getMailMerge().executeWithRegions(new DataTable(rs, tableName));
	}

	/**
	 * 删除文档中的段落内容
	 * 
	 * @param paragraphName
	 *            段落名(对应着文档是以对mergeFiled如：<<DelStart:tt>> <<DelEnd:tt>>)
	 * @throws Exception
	 */
	public void deleteParagraph(String paragraphName) {

	}

	/**
	 * 删除文档中的所有邮件合并域
	 * 
	 * @throws Exception
	 */
	public void deleteMegreField() {
		doc.getMailMerge().deleteFields();
	}

	/**
	 * 删除开始和结束标签内容中表格之间的空格
	 * 
	 * @param startTag
	 * @param endTag
	 * @throws Exception
	 */
	public void deleteTablesOfSpace(String startTag, String endTag)
			throws Exception {
		Document doc = this.getCurrentDocument();
		DocumentBuilder builder = new DocumentBuilder(doc);
		builder.moveToMergeField(startTag, false, false);
		Node nodeStart = builder.getCurrentParagraph();
		builder.moveToMergeField(endTag, false, false);
		Node nodeEnd = builder.getCurrentParagraph();
		int start = nodeStart.getParentNode().indexOf(nodeStart);
		int end = nodeEnd.getParentNode().indexOf(nodeEnd);
		for (int i = end - 1; i >= start + 1; i--) {
			Node temp = nodeStart.getParentNode().getChildNodes().get(i);
			if (temp != null && NodeType.PARAGRAPH == temp.getNodeType()) {
				temp.remove();
			}
		}

	}

}
