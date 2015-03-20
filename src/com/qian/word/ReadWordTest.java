package com.qian.word;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import com.aspose.words.Body;
import com.aspose.words.Cell;
import com.aspose.words.CompositeNode;
import com.aspose.words.Document;
import com.aspose.words.DocumentBuilder;
import com.aspose.words.ImportFormatMode;
import com.aspose.words.Node;
import com.aspose.words.NodeCollection;
import com.aspose.words.NodeType;
import com.aspose.words.Paragraph;
import com.aspose.words.Row;
import com.aspose.words.Run;
import com.aspose.words.Section;
import com.aspose.words.Table;
import com.qian.word.content.BasicWordController;

/**
 * 读取特定格式下面的单元格内容
 * @author qianwangpeng
 *
 */
public class ReadWordTest {
	Document ndoc;
	DocumentBuilder db;
	BasicWordController bwc= new BasicWordController();
	Document doc1=null;
	static final String INPUTDIC="f:/doc/";//读取目录
	static final String OUTDIC="f:/pdf/";//输出目录
	static	StringBuffer sb=new StringBuffer();//日志内容
	static final String WRITEFILE="E:/intro.sql";//日志文件目录

	
	
	public static void main(String[] args) {
		
	}

	//读取word
	public  void readFile(){
		File file = new File(INPUTDIC);
		File[] files=null;
		if(file.isDirectory()){
			files=file.listFiles();
		}
		if(files==null){
			files=new File[0];
		}
		for(File f:files){
			if(f.isFile()){
					readDoc(f);
			}
		}
		getWrite();
	}
	
	
	
	
	public  void readDoc(File f){
	BasicWordController bwc= new BasicWordController();
		
		try {
			Document doc=bwc.openDocument(new FileInputStream(f));
			createDoc();
			parseNode(doc,ndoc);
			saveDoc(f);
		} catch (FileNotFoundException e) {
			sb.append(f.getName()+"文件未找到\r\n");
		} catch (Exception e) {
			sb.append("异常文件名"+f.getName()+"异常理由："+e.toString());
		}
	}
	
	/**
	 * 需要的内容截取--递归
	 * @param node
	 * @param ncn
	 * @throws Exception
	 */
	public  void parseNode(Node node,CompositeNode ncn) throws Exception{
		switch (node.getNodeType())
		{
			case NodeType.DOCUMENT:{
				Document doc =(Document) node;
				NodeCollection<?> nc=doc.getChildNodes();
				Iterator<?> itr=nc.iterator();
				while(itr.hasNext()){
					Node nd=(Node) itr.next();
					parseNode(nd,ncn);
				}
				break;
				
			}
			case NodeType.SECTION:{
				Section section =(Section) node;
				Section ns = (Section) ndoc.importNode(section, false, ImportFormatMode.KEEP_SOURCE_FORMATTING);
				NodeCollection<?> nc=section.getChildNodes();
				Iterator<?> itr=nc.iterator();
				while(itr.hasNext()){
					Node nd=(Node) itr.next();
					parseNode(nd,ns);
				}
				break;
			}
			case NodeType.BODY:{
				Body body=(Body) node;
				Body nbody = (Body) ndoc.importNode(body, false, ImportFormatMode.KEEP_SOURCE_FORMATTING);
				NodeCollection<?> nc=body.getChildNodes();
				Iterator<?> itr=nc.iterator();
				while(itr.hasNext()){
					Node nd=(Node) itr.next();
					parseNode(nd,nbody);
				}
				break;
			}
			case NodeType.TABLE:{
				Table table = (Table) node; 
				Table ntable= (Table) ndoc.importNode(table, false,ImportFormatMode.KEEP_SOURCE_FORMATTING);
				NodeCollection<?> rows=table.getChildNodes();
				
				for(int i=0;i<2;i++){
					NodeCollection<?> cell=((Row)rows.get(i)).getChildNodes();
					if(i==0){
						int order=Integer.valueOf(((Cell)cell.get(1)).getParagraphs().get(0).getRuns().get(0).getText());
						String name=((Cell)cell.get(4)).getParagraphs().get(0).getText();
						name=name.substring(0,name.length()-1).replace(" ", "").replace("　", "");
					}
					if(i==1){
						String idcard=((Cell)cell.get(5)).getText();
					}
				}
				
				
				
				
				
				//对表中数据进行处理
//				Iterator<?> itr=rows.iterator();
//				while(itr.hasNext()){
//					Row nr=(Row) itr.next();
//					Row nrow=(Row) ndoc.importNode(nr, true,ImportFormatMode.KEEP_SOURCE_FORMATTING);
//					ntable.appendChild(nrow);
//				}
				break;
			}
			case NodeType.PARAGRAPH:{
				Paragraph para=(Paragraph) node;
				Paragraph npara=(Paragraph) ndoc.importNode(para, false,ImportFormatMode.KEEP_SOURCE_FORMATTING);
				NodeCollection<?> nc=para.getChildNodes();
				Iterator<?> itr=nc.iterator();
				while(itr.hasNext()){
					Node nd=(Node) itr.next();
					parseNode(nd,npara);
				}
				break;
			}
			case NodeType.RUN:{
				Run run=(Run) node;
				Run nrun=(Run) ndoc.importNode(run, true,ImportFormatMode.KEEP_SOURCE_FORMATTING);
				break;
			}
		} 	
		
	}

	public void saveDoc(File f){
		try {
			ndoc.getFirstChild().remove();
			ndoc.save(OUTDIC+"/"+ f.getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		//创建新word
	public  void createDoc(){
		try {
			ndoc =new Document();
			db=new DocumentBuilder(ndoc);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	//输出日志文件
		public void getWrite(){
			try {
				BufferedWriter bw=new BufferedWriter(new FileWriter(WRITEFILE));
				 bw.write(sb.toString());//重新写文件
				 bw.close();
			} catch (IOException e) {
				System.out.println("文件生成有误\r\n");
			}
		}
}


