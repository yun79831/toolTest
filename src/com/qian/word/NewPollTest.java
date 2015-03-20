package com.qian.word;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
 * 单元格合并操作
 * @author admin
 *
 */
public class NewPollTest {
	Document ndoc;
	DocumentBuilder db;
	static String zm="C:/Users/Administrator/Desktop/";
	public static void main(String[] args) {
		NewPollTest np =new NewPollTest();
		np.readFile();
	}
	
	public  void readFile(){
		
		File file = new File(zm+"2013/");
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
			//break;
			
		}
	}
	public  void readDoc(File f){
		BasicWordController bwc= new BasicWordController();
		
		try {
			Document doc=bwc.openDocument(new FileInputStream(f));
			createDoc();
			parseNode(doc,ndoc);
			saveDoc(f);
			} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	public  void parseNode(Node node,CompositeNode ncn){
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
				ncn.appendChild(ns);
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
				ncn.appendChild(nbody);
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
				ncn.appendChild(ntable);
				NodeCollection<?> row=table.getChildNodes();
				int r=row.getCount();
				Row[] nRow=new Row[2];
				Cell[][] title = new Cell[2][];
				for(int i=0;i<2;i++){
					NodeCollection<?> cell=((Row)row.get(i)).getChildNodes();
					nRow[i]=(Row) ndoc.importNode(row.get(i), false,ImportFormatMode.KEEP_SOURCE_FORMATTING);
					int c=cell.getCount();
					int cls=c+3;
					Cell[] titl=new Cell[cls];
					for(int j=0;j<c;j++){
						Cell cel=(Cell)ndoc.importNode(cell.get(j), true,ImportFormatMode.KEEP_SOURCE_FORMATTING);
						titl[j]=cel;
						if(j==0){
							titl[c]=(Cell) cel.deepClone(true);
							titl[c+1]=(Cell) cel.deepClone(true);
							titl[c+2]=(Cell) cel.deepClone(true);
							if(i==0){
								titl[c].getParagraphs().get(0).getRuns().get(0).setText("加权票数");
								titl[c+1].getParagraphs().get(0).getRuns().get(0).setText("累计票数");
								titl[c+2].getParagraphs().get(0).getRuns().get(0).setText("计票等级");
							}
						}
					}
					title[i]=titl;
				}
				for(int i=0;i<2;i++){
					ntable.appendChild(nRow[i]);
					for(int j=0;j<title[i].length;j++){
						int jw=0;
						if(j==0){
							jw=20;
						}else if(j==1){
							jw=15;
						}else if(j==title[i].length-4){
							jw=20;
						}
						if(j<=1||j>title[i].length-5){
							double width=title[i][j].getCellFormat().getWidth();
							title[i][j].getCellFormat().setWidth(width-jw);
						}
						nRow[i].appendChild(title[i][j]);
					}
				}
				
				
				
				
				List<String[]> list = new ArrayList<String[]>();
				List<String[]> onelist = new ArrayList<String[]>();
				List<String[]> twolist = new ArrayList<String[]>();
				List<String[]> threelist = new ArrayList<String[]>();
				List<String[]> notlist = new ArrayList<String[]>();
				int c =((Row)row.get(2)).getChildNodes().getCount();
				Cell[] style=new Cell[c+3];
				Row rowStyle=(Row) ndoc.importNode(row.get(2),false,ImportFormatMode.KEEP_SOURCE_FORMATTING);
				for(int i=2;i<r;i++){
					NodeCollection<?> cell=((Row)row.get(i)).getChildNodes();
					String[] object= new String[c+3]; //多出的3个位加权票，累计票，符合等级
					
					for(int j=0;j<c;j++){
						Cell cel=(Cell)ndoc.importNode(cell.get(j), true,ImportFormatMode.KEEP_SOURCE_FORMATTING);
						object[j]=cel.getText().trim();
						if(i==2){
							style[j]=cel;
							if(j==0){
							style[c]=cel;
							style[c+1]=cel;
							style[c+2]=cel;
							}
						}
					}
					
					double[] weigth={1,0.85,0.792};
					double[] w1={1,0.87,0.819};
					double[] w2={1,0.89,0.847};
					double[] w3={1,0.91,0.867};
					int one=Integer.parseInt(String.valueOf(object[2]));
					int two=Integer.parseInt(String.valueOf(object[3]));
					int three=Integer.parseInt(String.valueOf(object[4]));
					int noT=Integer.parseInt(String.valueOf(object[5]));
					int expNum=one+two+three+noT;
					
					if(expNum>=17&&expNum<=20){
						weigth=w1;
					}else if(expNum>=21&&expNum<=24){
						weigth=w2;
					}else if(expNum>=25){
						weigth=w3;
					}
					
					if(one*2>=expNum){
						BigDecimal bg = new BigDecimal(one*weigth[0]);
						object[c]=String.valueOf(bg.setScale(4, BigDecimal.ROUND_HALF_UP ).doubleValue());
						object[c+1]=String.valueOf(one);
						object[c+2]="1";
						onelist.add(object);
					}else if((one+two)*2>expNum){
						BigDecimal bg = new BigDecimal(one*weigth[0]+two*weigth[1]);
						object[c]=String.valueOf(bg.setScale(4, BigDecimal.ROUND_HALF_UP ).doubleValue());
						object[c+1]=String.valueOf(one+two);
						object[c+2]="2";
						twolist.add(object);
					}
					else if((one+two+three)*2>expNum){
						BigDecimal bg = new BigDecimal(one*weigth[0]+two*weigth[1]+three*weigth[2]);
						object[c]=String.valueOf(bg.setScale(4, BigDecimal.ROUND_HALF_UP ).doubleValue());
						object[c+1]=String.valueOf(one+two+three);
						object[c+2]="3";
						threelist.add(object);
					}else{
						BigDecimal bg = new BigDecimal(one*weigth[0]+two*weigth[1]+three*weigth[2]);
						object[c]=String.valueOf(bg.setScale(4, BigDecimal.ROUND_HALF_UP ).doubleValue());
						object[c+1]=String.valueOf(one+two+three);
						object[c+2]="4";
						notlist.add(object);
					}
					
				}
				list.addAll(onelist);//处理排序功能已被删除
				list.addAll(twolist);
				list.addAll(threelist);
				list.addAll(notlist);
				for(String[] str:list){
					Row rowtemp=(Row) rowStyle.deepClone(false);
					ntable.appendChild(rowtemp);
					for(int i=0;i<style.length;i++){
						int jw=0;
						if(i==0){
							jw=20;
						}else if(i==1){
							jw=15;
						}else if(i==style.length-4){
							jw=20;
						}
						Cell celltemp=(Cell) style[i].deepClone(true);
						celltemp.getParagraphs().get(0).getRuns().get(0).setText(str[i]);
						if(i<=1||i>style.length-5){
							double width=celltemp.getCellFormat().getWidth();
							celltemp.getCellFormat().setWidth(width-jw);
						}
						rowtemp.appendChild(celltemp);
					}
				}
				break;
			}
			 
			case NodeType.PARAGRAPH:{
				Paragraph para=(Paragraph) node;
				Paragraph npara=(Paragraph) ndoc.importNode(para, false,ImportFormatMode.KEEP_SOURCE_FORMATTING);
				ncn.appendChild(npara);
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
				ncn.appendChild(nrun);
				break;
			}
		}
		
	}
	public  void createDoc(){
		
		try {
			ndoc =new Document();
			db=new DocumentBuilder(ndoc);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void saveDoc(File f){
		try {
			ndoc.getFirstChild().remove();
			ndoc.save(zm+"2013edit/"+ f.getName());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
