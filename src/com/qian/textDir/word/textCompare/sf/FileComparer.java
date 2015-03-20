package com.qian.textDir.word.textCompare.sf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.ansj.domain.Term;
import org.ansj.splitWord.Analysis;
import org.ansj.splitWord.analysis.ToAnalysis;

import com.qian.textDir.word.textCompare.sf.model.IntegerWrap;
import com.qian.textDir.word.textCompare.sf.model.LineObject;
import com.qian.textDir.word.textCompare.sf.model.LineResultObject;
import com.qian.textDir.word.textCompare.sf.model.StringLineObject;

public class FileComparer {
	
	public static String ENCODE="UTF-8";
	
	public static Set<String> ignoreWords = new HashSet<String>();
	
	public static String word2String(File word) throws Exception{
		TextExtractor te = new TextExtractor();
		te.process(word);
		return te.getString();
	}
	
	public static List<String> word2Lines(File word) throws Exception{
		TextExtractor te = new TextExtractor();
		te.process(word);
		List<String> ret = te.toLines();
		return ret;
	}
	
	/**
	 * 初始化加载分词库
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static void loadIgnoreWords() throws IOException, URISyntaxException{
		URL url = new URL(FileComparer.class.getResource("/")+"com/qian/textDir/word/ignoreWords.word" );
		File file  = new File(url.toURI());
		BufferedReader rd1 = new BufferedReader( new InputStreamReader(new FileInputStream(file), ENCODE) );
		try{
			String word = rd1.readLine();
			while(word!=null){
				if (word.trim().length()>0)
					ignoreWords.add(word.trim());
				word = rd1.readLine();
			}
		}
		finally{
			rd1.close();
		}
	}
	/**
	 * 分词排除(核心方法)
	 * @param rd
	 * @return
	 * @throws IOException
	 */
	public static Map<String, IntegerWrap> calcTermFrequency(Reader rd) throws IOException{
		Analysis udf = new ToAnalysis(rd);
		Term term = null ;
		Map<String, IntegerWrap> result = new HashMap<String, IntegerWrap>();
		
		while((term=udf.next())!=null){
			String word = term.getName();
			
			if (ignoreWords.contains(word))//特殊词排除
				continue;
			if(word.trim().length()==0)//空字符排除
				continue;
			String str = "~!@#$%^&*()_+{}[]:\";'<>?,./\\|？！。；‘【】·、，　（）《》｛｝";
			if (str.indexOf(word.trim())>0)//特殊符号排除
				continue;
			
			if (result.get(word)==null){
				result.put(word, IntegerWrap.newOne());
				result.get(word).value++;
			}
			else{
				result.get(word).value++;
			}
		}
		return result;		
	}
	public static List<LineObject> prepareLine(List<StringLineObject> strs) throws IOException{
		List<LineObject> ret = new ArrayList<LineObject>();
		for(int i=0;i<strs.size(); i++){
			StringLineObject sso = strs.get(i); 
			String str = sso.str;
			Reader rd1 =new StringReader(str);
			Map<String, IntegerWrap> tf = calcTermFrequency(rd1);
			LineObject lo = new LineObject();
			lo.str = str;
			lo.tf = tf;
			lo.lineNum = i;
			lo.part = sso.part;
			ret.add(lo);
		}
		return ret;
	}
	
	public static double calcSimilarity_bak(Map<String, Integer> in1, Map<String, Integer> in2){
		if (in1==null || in2==null) return 0;
		long in1MulIn2 = 0;
		long in1Square=0;
		long in2Square=0;
		Set<String> keys1 = in1.keySet();
		Set<String> keys2 = in2.keySet();
		Set<String> keys = new HashSet<String>();
		keys.addAll(keys1);
		keys.addAll(keys2);
		for(String key: keys){
			Integer i1 = in1.get(key);
			Integer i2 = in2.get(key);
			if (i1==null) i1 = 0;
			if (i2==null) i2 = 0;
			in1MulIn2 += i1*i2;
			
			in1Square +=  i1*i1;
			in2Square +=  i2*i2;
		}
		return in1MulIn2 / Math.sqrt(in1Square*in2Square);		
	}
	
	//全文大概相似度
	public static double compareStrings(String str1, String str2, Map<String, Map<String, IntegerWrap>>cache) throws IOException{
		
		Map<String, IntegerWrap> tf1 = null;
		Map<String, IntegerWrap> tf2 = null;
		if (cache!=null){
			tf1 = cache.get(str1);
			tf2 = cache.get(str2);
		}
		if (tf1==null){
			Reader rd1 =new StringReader(str1);
			tf1 = calcTermFrequency(rd1);
			if (cache!=null)
				cache.put(str1, tf1);
		}
		if (tf2==null){
			Reader rd2 =new StringReader(str2);
			tf2 = calcTermFrequency(rd2);
			if (cache!=null)
				cache.put(str2, tf2);
		}		
		double ret = calcSimilarity(tf1, tf2);
		return ret;
	}
	
	
	private static  List<LineResultObject> sortLines(Map<String, LineResultObject> map) {
		 List<LineResultObject> list = new ArrayList<LineResultObject>(map.values());
	     Collections.sort(list, new Comparator<LineResultObject>() {
	          public int compare(LineResultObject o1, LineResultObject o2) {
	               return o2.similarity>o1.similarity?1:0;
	          }
	     });
	    return list;
	} 
	
	
	private static  List<LineResultObject> sortLines2(Map<String, LineResultObject> map) {
		 List<LineResultObject> list = new ArrayList<LineResultObject>(map.values());
	     Collections.sort(list, new Comparator<LineResultObject>() {
	          public int compare(LineResultObject o1, LineResultObject o2) {
	               return o2.leftLineNum-o1.leftLineNum;
	          }
	     });
	    return list;
	} 
	

	
	public static List<LineResultObject> compareLines(List<String> ls1, List<String> ls2, double similarity)throws IOException{
		Map<String, Map<String, IntegerWrap>>cacheMap = new HashMap<String, Map<String, IntegerWrap>>();
		Map<String, LineResultObject> result = new HashMap<String, LineResultObject>();
		Set<Integer> ignoreLines=new HashSet<Integer>();
		for(int i=0; i<ls1.size(); i++){
			String line1 = ls1.get(i);
			for(int j=0; j<ls2.size(); j++){
				if (ignoreLines.contains(j)){
					continue;
				}
				String line2 = ls2.get(j);
				double ret = compareStrings(line1, line2, cacheMap);
				if (ret<similarity)
					continue;
				ignoreLines.add(j);
				LineResultObject lo = result.get(line1);
				if (lo==null)
				{
					lo = new LineResultObject();
					lo.line1 = line1;
					lo.line2 = line2;
					lo.similarity = ret;
					result.put(line1, lo);
				}
				else{
					if (lo.similarity<ret)
					{
						lo.line2 = line2;
						lo.similarity = ret;
					}
				}
				break;
			}
		}
		return sortLines(result);
	}
	
	
	/**
	 * 主要语句对比的方法
	 * @param ls1
	 * @param ls2
	 * @param similarity
	 * @return
	 * @throws IOException
	 */
	public static List<LineResultObject> compareLines2(List<LineObject> ls1, List<LineObject> ls2, double similarity)throws IOException{
		long t = System.currentTimeMillis();
		Map<String, LineResultObject> result = new HashMap<String, LineResultObject>();
		Set<Integer> ignoreLines=new HashSet<Integer>();
		for(int i=0; i<ls1.size(); i++){
			LineObject line1 = ls1.get(i);
			if(isFormat(line1==null?null:line1.str))
			continue;
			for(int j=0; j<ls2.size(); j++){
//				if (ignoreLines.contains(j)){
//					continue;
//				}
				LineObject line2 = ls2.get(j);
				double ret = calcSimilarity(line1.tf, line2.tf);
//				if (ret<similarity)
//					continue;
				//ignoreLines.add(j);
				LineResultObject lo = result.get(line1.str);
				if(isFormat(line2==null?null:line2.str))
					continue;
				if (lo==null)
				{
					lo = new LineResultObject();
					lo.line1 = line1.str;
					lo.line2 = line2.str;
					lo.leftLineNum = line1.lineNum;
					lo.rightLineNum = line2.lineNum;
					lo.similarity = ret;
					result.put(line1.str, lo);
				}
				else{
					if (lo.similarity<ret)
					{
						lo.line2 = line2.str;
						lo.rightLineNum = line2.lineNum;
						lo.similarity = ret;
					}
				}
				//break;
			}
		}
		return sortLines(result);
	}
	/**
	 * 单句根据分词比较相似度
	 * @param in1
	 * @param in2
	 * @return
	 */
		public static double calcSimilarity(Map<String, IntegerWrap> in1, Map<String, IntegerWrap> in2){
			if (in1==null || in2==null) return 0;
			if (in1.size()==0 || in2.size()==0)return 0;
			long in1MulIn2 = 0;
			long in1Square=0;
			long in2Square=0;
		
			Set<Entry<String, IntegerWrap>> es = in1.entrySet();
			Iterator<Entry<String, IntegerWrap>> entrySetIterator = es.iterator();
			
			while(entrySetIterator.hasNext()){
				Entry<String, IntegerWrap> entry = entrySetIterator.next();
				IntegerWrap i1 = entry.getValue();//in1.get(key);
				IntegerWrap i2 = in2.get(entry.getKey());
				
				//if (i1==null) i1 = IntegerWrap.newOne();
				if (i2==null) i2 = IntegerWrap.newOne();
				in1MulIn2 += i1.value * i2.value;
				
				in1Square +=  i1.value * i1.value;
				in2Square +=  i2.value * i2.value;
			}
			
			es = in2.entrySet();
			entrySetIterator = es.iterator();
			while(entrySetIterator.hasNext()){
				Entry<String, IntegerWrap> entry = entrySetIterator.next();
				IntegerWrap i1 = in1.get(entry.getKey());
				if (i1!=null)
					continue;
				IntegerWrap i2 = entry.getValue();
				if (i1==null) i1 = IntegerWrap.newOne();
				//if (i2==null) i2 = IntegerWrap.newOne();
				in1MulIn2 += i1.value * i2.value;
				
				in1Square +=  i1.value * i1.value;
				in2Square +=  i2.value * i2.value;
			}
			return in1MulIn2 / Math.sqrt(in1Square*in2Square);		
		}
		
		/**
		 * 计算权重不同相似度占的比重不一样
		 * @param list
		 * @param ls1
		 * @param NO1
		 * @param NO2
		 * @param NO3
		 * @return
		 */
		public static double getsim(List<LineResultObject> list,List<LineObject> ls1,final double XS,final double NO1,final  double NO2,final  double NO3){
			double countmax=0;
			double aboutCount=0;

		for (LineObject lineObject : ls1) {
			countmax+=lineObject.tf.size();
		}
	
		for(LineResultObject liner: list){
			if(liner.getSimilarity()>XS){
			
				LineObject leftline=null;
				leftline = ls1.get(liner.leftLineNum);
			
				if(liner.getSimilarity()>XS&&liner.getSimilarity()<=(XS+0.1)){//最低限度级别
					aboutCount+=(leftline.tf.size()*NO1);
				}
				if(liner.getSimilarity()>(XS+0.1)&&liner.getSimilarity()<=(XS+0.2)){	
					aboutCount+=(leftline.tf.size()*NO2);
				}
				if(liner.getSimilarity()>(XS+0.2)){	
					aboutCount+=(leftline.tf.size()*NO3);
				}
			}
		}
			return aboutCount/countmax;
		}
		
		/**
		 * 计算权重不同相似度占的比重不一样 --综合的
		 * @param list
		 * @param ls1
		 * @param NO1
		 * @param NO2
		 * @param NO3
		 * @return
		 */
		public static double getSimSum(List<LineResultObject> list,List<LineObject> ls1,final double XS,final double NO1,final  double NO2,final  double NO3){
			double countmax=0;
			double aboutCount=0;
			
		     Collections.sort(list, new Comparator<LineResultObject>() {
		          public int compare(LineResultObject o1, LineResultObject o2) {
		        	  int ret = o1.leftLineNum-o2.leftLineNum;
		        	  if(ret==0)
		        		  return o2.similarity>o1.similarity?1:0;
		        	  else
		        		  return ret;
		          }
		     });
		     List<LineResultObject> tmplist = new ArrayList<LineResultObject>();
		     int oldLineNo=-1;
		     for(LineResultObject lo: list){
		    	 if (oldLineNo==lo.leftLineNum){
		    		 continue;
		    	 }
		    	 oldLineNo = lo.leftLineNum;
		    	 tmplist.add(lo);
		     }
		     list = tmplist;
	
		for (LineObject lineObject : ls1) {
			countmax+=lineObject.tf.size();
		}
	
		for(LineResultObject liner: list){
			if(liner.getSimilarity()>XS){
			
				LineObject leftline = ls1.get(liner.leftLineNum);

				if(liner.getSimilarity()>XS&&liner.getSimilarity()<=(XS+0.1)){//最低限度级别
					aboutCount+=(leftline.tf.size()*NO1);
				}
				if(liner.getSimilarity()>(XS+0.1)&&liner.getSimilarity()<=(XS+0.2) ){	
					aboutCount+=(leftline.tf.size()*NO2);
				}
				if(liner.getSimilarity()>(XS+0.2)){	
					aboutCount+=(leftline.tf.size()*NO3);
				}
			}
		}
			return aboutCount/countmax;
		}
		
		
		
	/**
	 * 判断格式语句
	 * @author qianwangpeng
	 * @param str
	 * @return
	 */
	public static boolean isFormat(String str){
		str=str.trim();
		if(str==null)return false;
		if(str.length()<20){
		String lastChar=str.charAt(str.length()-1)+"";
		
		if (lastChar.trim().matches("[ 　0-9０-９a-zA-Z\u4e00-\u9fa5]")) {
			return true;
		}
		if (lastChar.trim().matches("[:：）)(（]")) {
			return true;
		}
		if(str.matches("所属学科：[\\s\\S]*[\\s\\S]*")){
			return true;
		}
		if(str.matches("研究结果：[\\s\\S]*[\\s\\S]*")){
			return true;
		}
		if(str.matches("旁证材料：[\\s\\S]*[\\s\\S]*")){
			return true;
		}
		if(str.matches("[\u4e00-\u9fa5]*：")){
			return true;
		}
		if(str.matches("[\u4e00-\u9fa5]*:")){
			return true;
		}
	
		if(str.matches("图\\w*[.]\\w*[\u4e00-\u9fa5]*：")){
			return true;
		}
		if(str.matches("[\u4e00-\u9fa5]、[\u4e00-\u9fa5]*：")){
			return true;
		}
		}
		return false;
	}
	/**
	 *调整页面的输出
	 * @param c
	 * @param left
	 * @param right
	 */
	public  static void prepareHtml(List<LineResultObject> c, List<LineObject> left, List<LineObject> right){
		for(LineResultObject liner: c){
			if(liner.getSimilarity()>0.5){
				LineObject leftline = left.get(liner.leftLineNum);
				LineObject rightline = right.get(liner.rightLineNum);	
				if(liner.getSimilarity()>0.5&&liner.getSimilarity()<=0.6){//最低限度级别
				leftline.rank="3";
				rightline.rank="3";
				}
				if(liner.getSimilarity()>0.6&&liner.getSimilarity()<=0.7 ){	
					leftline.rank="2";
					rightline.rank="2";
				}
				if(liner.getSimilarity()>0.7){	
					leftline.rank="1";
					rightline.rank="1";
				}
			leftline.htmlTarget = "right_"+liner.rightLineNum;
			rightline.htmlTarget ="right_"+liner.rightLineNum;
			}
		}
	}

	public static double compareFiles(File f1, File f2) throws IOException{
		Reader rd1 = new BufferedReader( new InputStreamReader(new FileInputStream(f1), ENCODE) );
		Map<String, IntegerWrap> tf1=null;
		Map<String, IntegerWrap> tf2 = null;
		try{
			tf1 = calcTermFrequency(rd1);
		}
		finally{
			rd1.close();
		}
		Reader rd2 = new BufferedReader( new InputStreamReader(new FileInputStream(f2), ENCODE) );
		try{
			tf2 = calcTermFrequency(rd2);
		}
		finally{
			rd2.close();
		}
		return calcSimilarity(tf1, tf2);
	}
	
	public static List<LineResultObject> compareFilesByLine(File file1, File file2, double similarity) throws Exception{
		List<String> lines1 = word2Lines(file1);
		List<String> lines2 = word2Lines(file2);
		List<LineResultObject> list = null;
		if (lines1.size()>lines2.size())
			list = compareLines(lines1, lines2, similarity);
		else
			list = compareLines(lines2, lines1, similarity);
		return list;	
	}
	//全文相似度
	public static void main_old(String...strings) throws Exception{
		System.out.println("初始化....");
		compareStrings("aaa", "bbb", null);
		loadIgnoreWords();// 初始化查重
		URL url = new URL("file:/e:/upload/"+"硕士研究生学位论文开题报告V4.doc" );
		File file1  = new File(url.toURI());
		String str1 = word2String(file1);
		url = new URL( "file:/e:/upload/"+"4904080059.doc" );
		File file2  = new File(url.toURI());
		String str2 = word2String(file2);
		compareStrings(str1, str2, null);
	}
	
	public static void main(String...strings) throws Exception{ 
	
		System.out.println("初始化....");
		
		compareStrings("aaa", "bbb", null);
		loadIgnoreWords();// 初始化查重


		System.out.println("初始化完成");
		System.out.println("word 行预处理");
		
	//	URL url = new URL( FileComparer.class.getResource("/")+"word/硕士研究生学位论文开题报告V4.doc" );
		File file1   = new File("E:/temp1.docx");
	//	url = new URL( FileComparer.class.getResource("/")+"word/4904080059.doc" );
		File file2  = new File("E:/temp2.docx");
		
		
		//word 文档
		List<StringLineObject> ratioline1 = TextExtractor.wordToLines(file1
				.toURI().toString());
		List<LineObject> ratiolines1 = FileComparer.prepareLine(ratioline1);
		
		List<StringLineObject> ratioline2 =  TextExtractor.wordToLines(file2
				.toURI().toString());
		List<LineObject> ratiolines2 = FileComparer.prepareLine(ratioline2);
		
		for (LineObject lineObject : ratiolines2) {
			System.out.println(lineObject.str+":");
			Map<String, IntegerWrap> map=lineObject.tf;
			for (String	 s : map.keySet()) {
				
				System.out.print(s+"-");
			}
		}
		
//			List<StringLineObject> pointline1 = TextExtractor.toLines("");
//			List<LineObject>	pointlines1 = FileComparer.prepareLine(pointline1);
//			List<StringLineObject> pointline2 = TextExtractor.toLines("");
//			List<LineObject>	pointlines2 = FileComparer.prepareLine(pointline2);
		
		
		
//			List<LineResultObject> list = new ArrayList<LineResultObject>();
//			list = FileComparer.compareLines2(ratiolines1, ratiolines2, 0.7);
//			System.out.println("进行了比较");
			//显示相似部分
//			for (LineResultObject lineResultObject : list) {
//				System.out.println(lineResultObject.toString());
//			}
	
	
	}
	


}
