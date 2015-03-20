package com.qian.textDir.word.textCompare.sf;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.detect.Detector;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;

import com.aspose.words.Body;
import com.aspose.words.Cell;
import com.aspose.words.Document;
import com.aspose.words.Node;
import com.aspose.words.NodeCollection;
import com.aspose.words.Paragraph;
import com.aspose.words.Section;
import com.aspose.words.Table;
import com.qian.textDir.word.textCompare.sf.model.StringLineObject;

public class TextExtractor {
	private OutputStream outputstream;
    private ParseContext context;
    private Detector detector;
    private Parser parser;
    private Metadata metadata;
    private String extractedText;

    public TextExtractor() {
        context = new ParseContext();
        detector = new DefaultDetector();
        parser = new AutoDetectParser(detector);
        context.set(Parser.class, parser);
        outputstream = new ByteArrayOutputStream();
        metadata = new Metadata();
    }
    
    public void process(File file) throws Exception {
        URL url = file.toURI().toURL();
        InputStream input = TikaInputStream.get(url, metadata);
        ContentHandler handler = new BodyContentHandler(outputstream);
        parser.parse(input, handler, metadata, context); 
        input.close();
    }

    public void process(String filename) throws Exception {
        URL url;
        File file = new File(filename);
        if (file.isFile()) {
            url = file.toURI().toURL();
        } else {
            url = new URL(filename);
        }
        InputStream input = TikaInputStream.get(url, metadata);
        ContentHandler handler = new BodyContentHandler(outputstream);
        parser.parse(input, handler, metadata, context); 
        input.close();
    }

    public String getString() {
        //Get the text into a String object
        extractedText = outputstream.toString();
        //Do whatever you want with this String object.
        //System.out.println(extractedText);
        return extractedText;
    }
    
    

/**
 * 大段文字转换成StringLineObject行对象
 * @param tmp
 * @return
 */
    public static List<StringLineObject> toLines(String tmp){
    	char ch=' ';
    	String line ="";
    	List<StringLineObject> lines = new ArrayList<StringLineObject>();
    	for(int i=0; i<tmp.length();i++){
    		ch = tmp.charAt(i);
    		if (/*ch==',' || ch=='，' ||*/ ch=='。'||ch=='!' ||ch=='！'||ch==';' ||ch=='；'){
    		if(line.trim().length()>0){
    			StringLineObject slo = new StringLineObject();
    			slo.str = line.replaceAll("\\n", "").replaceAll("\\r", "").trim()+ch;
    			slo.part = 0;
    			lines.add(slo);//排除整行空格
    		}
			line = "";
			continue;
    		}
    		if (ch!='\r'||ch!='\n')
    			line = line + ch;
    	}
    	return lines;
    }
    
    /**
	 * word转换，带行号
	 * @param filename
	 * @return
	 * @throws Exception
	 */
    @SuppressWarnings("rawtypes")
	public static List<StringLineObject> wordToLines(String filename) throws Exception{
    	if (filename.indexOf("file:/")==0){
    		filename = filename.substring("file:/".length());
    	}
    	Document doc = new Document(filename);
    	List<StringLineObject> result = new ArrayList<StringLineObject>();
		NodeCollection nc = doc.getChildNodes();
		int part=0;
		Node tmpNode=null;
		
		for(int l=0; l<nc.getCount(); l++){
			Section n = (Section)nc.get(l);
			NodeCollection nc2 = n.getChildNodes();
			for(int j=0; j<nc2.getCount(); j++){
				tmpNode = nc2.get(j);
				if (!(tmpNode instanceof Body))
					continue;
				Body n2 = (Body)nc2.get(j);
				NodeCollection nc3 = n2.getChildNodes();
				
				for(int x=0; x<nc3.getCount();x++){
					tmpNode = nc3.get(x);
					if (tmpNode instanceof Table)
					{
						String tmp1 = tmpNode.getText();
						if(tmp1.indexOf("总体思路")!=-1&&x<4){
							Table	table = (Table)tmpNode;
							Cell c=table.getFirstRow().getFirstCell();
							if(c.getText().indexOf("总体思路、技术方案, 限15页")!=-1&&c.getText().length()<40&&table.getRows().getCount()>1)
								c=table.getRows().get(1).getFirstCell();
							nc3=c.getChildNodes();
							break;
						}
					}
						
				}
				for(int k=0; k<nc3.getCount(); k++){
					
					tmpNode = nc3.get(k);
					if (!(tmpNode instanceof Paragraph))
						continue;
					Paragraph n4 = (Paragraph)nc3.get(k);
					String tmp = n4.getText();
					char ch=' ';
					if (tmp.indexOf("Aspose.Words.")>0 || tmp.indexOf("using Aspose.Words")>0 || tmp.trim().length()==0)
						continue;
					part++;
					String line ="";
			    	for(int i=0; i<tmp.length();i++){
			    		ch = tmp.charAt(i);
			    		if (ch=='。' ||ch=='!' ||ch=='！' || i==tmp.length()-1){
			    			if (line.trim().length()==0)
			    				continue;
							StringLineObject slo = new StringLineObject();
							slo.str = line+ch;
							slo.part = part;
			    			result.add(slo);
			    			line = "";
			    			continue;
			    		}
			    		if (ch!='\r'||ch!='\n')
			    			line = line + ch;
			    	}
				}
				
			}
		}
		return result;
    }
    
    
    /**
     * 对其进行了修改
     * @return
     */
    public List<String> toLines(){
    	String tmp = this.getString();
    	char ch=' ';
    	String line ="";
    	List<String> lines = new ArrayList<String>();
    	for(int i=0; i<tmp.length();i++){
    		ch = tmp.charAt(i);
    		if (/*ch==',' || ch=='，' ||*/ ch=='。'||ch=='!' ||ch=='！'||ch==';' ||ch=='；'){
    		if(line.trim().length()>0)lines.add(line.replaceAll("\\n", "").replaceAll("\\r", "").trim());//排除整行空格
    			line = "";
    			continue;
    		}
    		if (ch!='\r'||ch!='\n')
    			line = line + ch;
    	}
    	return lines;
    }

    public static void main(String args[]) throws Exception {
        //if (args.length == 1) {
            TextExtractor textExtractor = new TextExtractor();
            String temp = TextExtractor.class.getResource("/")+"500729-999.doc";
            textExtractor.process(temp);
            textExtractor.getString();
//        } else { 
//            throw new Exception();
//        }
    }
}
