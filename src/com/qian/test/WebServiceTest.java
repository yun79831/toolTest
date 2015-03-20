package com.qian.test;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class WebServiceTest {
	public static void main(String[] args) throws MalformedURLException, Exception {
//		Client c=new Client(new URL("http://webservice.webxml.com.cn/WebServices/IpAddressSearchWebService.asmx?wsdl"));
//		Object[] obj=c.invoke("getCountryCityByIp", new Object[]{"210.76.97.218"});
//		System.out.println();

        URL url=new URL("http://webservice.webxml.com.cn/WebServices/IpAddressSearchWebService.asmx/getCountryCityByIp?theIpAddress=210.56.193.178");  
        URLConnection conn=url.openConnection();  
        conn.setUseCaches(false);  
       conn.setDoInput(true);//这里可以设置成false  
        conn.setDoOutput(true);  
        InputStream is=conn.getInputStream();    
        
        
        DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();  
        dbf.setNamespaceAware(true);  
        DocumentBuilder db=dbf.newDocumentBuilder();  
        Document     doc=db.parse(is);  
        NodeList nl=doc.getElementsByTagName("string");  
        for (int i = 0; i < nl.getLength(); i++) {
			System.out.println(nl.item(i).getFirstChild().getNodeValue());
		}
        is.close();  
	}
}
