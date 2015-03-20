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
 * ������word�������޸İ�
 * 
 * @author Ǯ����
 * 
 */
public class BasicWordController {
	private Document doc = null;

	/**
	 * ���Aspose.Word�����֤�ļ�
	 */
	static {
		try {
			License license = new License();
			license.setLicense(URLDecoder.decode(BasicWordController.class
					.getResource("Aspose.Words.lic").getFile(), "UTF-8"));

			if (System.getProperty("os.name").toLowerCase().indexOf("windows") == -1
					&& System.getProperty("os.name").toLowerCase()
							.indexOf("mac") == -1) {// ��linux�¼��������
				FontSettings.setFontsFolder("//usr//share//fonts", true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * ��һ����Word�ĵ�
	 * 
	 * @throws Exception
	 */
	public Document openNewDocument() throws Exception {
		doc = new Document();
		return doc;
	}

	/**
	 * ��Word�ĵ�
	 * 
	 * @param docPath
	 *            �ĵ���·��
	 * @param fileName
	 *            ���ĵ�����
	 * @throws Exception
	 */
	public Document openDocument(String docPath, String fileName)
			throws Exception {
		doc = new Document(docPath + fileName);
		return doc;
	}

	/**
	 * ��Word�ĵ�
	 * 
	 * @param stream
	 *            �ļ���
	 * @throws Exception
	 */
	public Document openDocument(InputStream stream) throws Exception {
		doc = new Document(stream);
		return doc;
	}

	/**
	 * ��õ�ǰ���Ƶ��ĵ�
	 * 
	 * @throws Exception
	 */
	public Document getCurrentDocument() throws Exception {
		return doc;
	}

	/**
	 * ��õ�ǰ���Ƶ��ĵ�
	 * 
	 * @throws Exception
	 */
	public void setCurrentDocument(Document doc) throws Exception {
		this.doc = doc;
	}

	/**
	 * �����ĵ�
	 * 
	 * @param docPath
	 *            ���ĵ�����·��
	 * @param fileName
	 *            �ĵ�����
	 * @param saveFormat
	 *            �ĵ�����ĸ�ʽ��������Ϣ�μ�com.mrg.kwmis.word.constant.SaveFormat��
	 * @throws Exception
	 */
	public void saveDocument(String docPath, String fileName, int saveFormat)
			throws Exception {
		doc.save(docPath + fileName, saveFormat);
	}

	/**
	 * �����ĵ�
	 * 
	 * @param outputStream
	 *            �������ĵ��������
	 * @param saveFormat
	 *            �ĵ�����ĸ�ʽ��������Ϣ�μ�com.mrg.kwmis.word.constant.SaveFormat��
	 * @throws Exception
	 */
	public void saveDocument(java.io.OutputStream outputStream, int saveFormat)
			throws Exception {
		doc.save(outputStream, saveFormat);
	}

	/**
	 * �ĵ�����
	 * 
	 * @param password
	 *            ����
	 * @throws Exception
	 */
	public void protectDocument(String password) throws Exception {
		doc.protect(ProtectionType.ALLOW_ONLY_FORM_FIELDS, password);
	}

	// -------------------------------�ļ������Ĳ���-----------------------------//
	/**
	 * ���������
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
	 * ���������
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
	 * �ļ�ת��Ϊ������
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

	// ****************************������Ĳ���*******************************//
	/**
	 * ����ѭ����
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
	 * ���������--��������
	 * 
	 * @param name
	 * @param obj
	 * @throws Exception
	 */
	public void mergeArray(String[] name, Object[] obj) throws Exception {
		doc.getMailMerge().execute(name, obj);
	}

	/***
	 * �򻯺����ӵ���
	 * 
	 * @param name
	 * @param obj
	 * @throws Exception
	 */
	public void mergeSingleValue(String name, Object obj) throws Exception {
		mergeArray(new String[] { name }, new Object[] { obj });
	}

	/**
	 * �򻯺����Ӷ��
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
	 * ���һ��������
	 * @param tableIndex
	 *            ������ĵ��е�˳��ţ���0��ʼ������
	 * @return
	 */
	public Table getTableByIndex(int tableIndex) throws Exception {
		return doc.getFirstSection().getBody().getTables().get(tableIndex);
	}

	/**
	 * �����ʼ������ƣ���ȡ�����µ�һ���ڵ�
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
	 * ���һ��������
	 * 
	 * @param tableName
	 *            ����
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
			// System.out.println("�ĵ���û��"+tableName+"�ı��");
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
			throw new Exception("�ĵ���û��" + nodeName + "�Ķ���");
		} else {
			return node;
		}
	}

	/**
	 * ��ñ���е�ָ����
	 * 
	 * @param table
	 *            ������ĵ��е�˳��ţ���0��ʼ������
	 * @param rowIndex
	 *            ����ţ���0��ʼ������
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
	 * ��ñ���е�ָ����
	 * 
	 * @param tableIndex
	 *            ������ĵ��е�˳��ţ���0��ʼ������
	 * @param rowIndex
	 *            ����ţ���0��ʼ������
	 * @return
	 */
	public Row getRow(int tableIndex, int rowIndex) throws Exception {
		return getRow(getTableByIndex(tableIndex), rowIndex);
	}

	/**
	 * ��ñ���е�ָ����
	 * 
	 * @param tableName
	 *            �������
	 * @param rowIndex
	 *            �����
	 * @return
	 */
	public Row getRow(String tableName, int rowIndex) throws Exception {
		return getRow(getTableByName(tableName), rowIndex);
	}

	/**
	 * ������е�ָ����
	 * 
	 * @param table
	 *            ������ĵ��е�˳��ţ���0��ʼ������
	 * @param rowIndex
	 *            ����ţ���0��ʼ������
	 * @param cellIndex
	 *            ����ţ���0��ʼ������
	 * @return
	 * @throws Exception
	 */
	public Cell getCell(Table table, int rowIndex, int cellIndex)
			throws Exception {
		return getCell(getRow(table, rowIndex), cellIndex);
	}

	/**
	 * ������е�ָ����
	 * 
	 * @param row
	 *            �ж���
	 * @param columnIndex
	 *            ����ţ���0��ʼ������
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
	 * ������е�ָ����
	 * 
	 * @param tableIndex
	 *            ������ĵ��е�˳��ţ���0��ʼ������
	 * @param rowIndex
	 *            ����ţ���0��ʼ������
	 * @param columnIndex
	 *            ����ţ���0��ʼ������
	 * @return
	 */
	public Cell getCell(int tableIndex, int rowIndex, int cellIndex)
			throws Exception {
		return getRow(getTableByIndex(tableIndex), rowIndex).getCells().get(
				cellIndex);
	}

	/**
	 * ������е�ָ����
	 * 
	 * @param tableName
	 *            �������
	 * @param rowIndex
	 *            ����ţ���0��ʼ������
	 * @param columnIndex
	 *            ����ţ���0��ʼ������
	 * @return
	 */
	public Cell getCell(String tableName, int rowIndex, int cellIndex)
			throws Exception {
		return getCell(getRow(getTableByName(tableName), rowIndex), cellIndex);
	}

	/**
	 * ��ֵ�Ԫ��
	 * 
	 * @param cell
	 *            Ҫ��ֵĵ�Ԫ�����
	 * @param splitCount
	 *            ��ָ���
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
	 * �ϲ���Ԫ��
	 * 
	 * @param cell
	 *            �ϲ���Χ�еĿ�ʼ��Ԫ�����
	 * @param horizontalMergeCount
	 *            ˮƽ�ϲ��ĵ�Ԫ�����(���ϲ���0)
	 * @param verticalMergeCount
	 *            ��ֱ�ϲ��ĵ�Ԫ�����(���ϲ���0)
	 * @throws Exception
	 */
	public void mergeCell(Cell cell, int horizontalMergeCount,
			int verticalMergeCount) throws Exception {
		if (horizontalMergeCount < 0)
			throw new Exception("ˮƽ�ϲ��ĵ�Ԫ���������С��0��");
		if (verticalMergeCount < 0)
			throw new Exception("��ֱ�ϲ��ĵ�Ԫ���������С��0��");
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
			int hxhs = 0; // ��ֱ�ϲ�ʱ��ѭ����ʵ�ʸ���
			int lxhs = 0;// ˮƽ�ϲ�ʱ��ѭ����ʵ�ʸ���
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
	 * ���ͼƬˮӡ
	 * 
	 * @param imageFilePath
	 *            ˮӡͼƬ·��
	 * @param imageFilePath
	 *            ˮӡͼƬ����
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
	 * ���һ��ͼƬ
	 * 
	 * @param imageFilePath
	 *            ͼƬ·��
	 * @param imageFilePath
	 *            ͼƬ����
	 * @throws Exception
	 */
	public void addImage(String imageFilePath, String imageFileName)
			throws Exception {

		DocumentBuilder builder = new DocumentBuilder(doc);
		builder.moveToDocumentEnd();// ���ĵ���β
		builder.insertBreak(BreakType.PAGE_BREAK);// �������ҳ��

		Shape shape = builder.insertImage(imageFilePath + imageFileName);
		shape.setWrapType(WrapType.NONE);// ʹͼ�񸡶�
		shape.setBehindText(true);// �Ƿ���ڻ��������

		// ��Ҫ��ʾ��ͼƬ�Ŀ�ȴ����ĵ��Ŀ��ʱ����ͼƬ������ó��ĵ��Ŀ��-10
		if (shape.getWidth() > builder.getPageSetup().getPageWidth()) {
			shape.setWidth(builder.getPageSetup().getPageWidth() - 10);
		}

		// ��Ҫ��ʾ��ͼƬ�ĸ߶ȴ����ĵ��ĸ߶�ʱ����ͼƬ�߶����ó��ĵ��ĸ߶�-10
		if (shape.getHeight() > builder.getPageSetup().getPageHeight()) {
			shape.setHeight(builder.getPageSetup().getPageHeight() - 10);
		}

		// ָ��ˮƽλ�úʹ�ֱλ��
		shape.setRelativeHorizontalPosition(RelativeHorizontalPosition.PAGE);
		shape.setHorizontalAlignment(HorizontalAlignment.CENTER);
		shape.setRelativeVerticalPosition(RelativeVerticalPosition.PAGE);
		shape.setVerticalAlignment(VerticalAlignment.CENTER);

	}

	/**
	 * ���PDF�ļ�
	 * 
	 * @param filePath
	 *            �ļ�·��
	 * @param fileName
	 *            �ļ�����
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
	 * ����ǩ����Ӧ��ֵ
	 * 
	 * @param list
	 *            LabelValueBean���󼯺�
	 * @throws Exception
	 */
	public void fillMergeField(ResultSet rs) throws Exception {
		// ��־����2011-6-17���޸Ĵ˴�
		// doc.getMailMerge().execute(rs);
		doc.getMailMerge().execute(new DataTable(rs));
	}

	/**
	 * ����ʼ��ϲ����������ֵ
	 * 
	 * @param tableName
	 *            ����
	 * @param rs
	 *            ResultSet����
	 * @throws Exception
	 */
	public void fillMergeField(String tableName, ResultSet rs) throws Exception {
		// doc.getMailMerge().executeWithRegions(tableName, rs);
		// ��־����2011-6-17���޸Ĵ˴�
		doc.getMailMerge().executeWithRegions(new DataTable(rs, tableName));
	}

	/**
	 * ɾ���ĵ��еĶ�������
	 * 
	 * @param paragraphName
	 *            ������(��Ӧ���ĵ����Զ�mergeFiled�磺<<DelStart:tt>> <<DelEnd:tt>>)
	 * @throws Exception
	 */
	public void deleteParagraph(String paragraphName) {

	}

	/**
	 * ɾ���ĵ��е������ʼ��ϲ���
	 * 
	 * @throws Exception
	 */
	public void deleteMegreField() {
		doc.getMailMerge().deleteFields();
	}

	/**
	 * ɾ����ʼ�ͽ�����ǩ�����б��֮��Ŀո�
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
