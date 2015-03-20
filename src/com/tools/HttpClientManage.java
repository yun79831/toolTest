package com.tools;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

public class HttpClientManage {
	public static int send(String url,Map<String, String> param) throws HttpException, IOException{
		HttpClient httpClient =new HttpClient();
			PostMethod postMethod = new PostMethod(url);
			NameValuePair[] data= null;
			if(param!=null){
				data=new NameValuePair[param.size()];
				int i=0;
				Set<String> keyset = param.keySet();
				for(String key : keyset)
				{
					data[i]=new NameValuePair(key, param.get(key));
					i++;
				}
				postMethod.addParameters(data);
			}
			 int stat=httpClient.executeMethod(postMethod);
			    return stat;
	}
	public static String send(String url) throws HttpException, IOException{
		HttpClient httpClient =new HttpClient();
		
		GetMethod getMethod = new GetMethod(url);
		int stat=httpClient.executeMethod(getMethod);
		 if (stat == HttpStatus.SC_OK) {
			 Cookie[] cookies= httpClient.getState().getCookies();
			 for (int i = 0; i < cookies.length; i++) {
				System.out.println(cookies[i].getName());
			}
			 if(cookies==null||cookies.length<1){
				 return "0";
			 }
			 for(int i=0;i<cookies.length;i++){
				 if("JSESSIONID".equalsIgnoreCase(cookies[i].getName()))
				 return cookies[i].getValue();
			 }
				
			}
			// ¶ÁÈ¡ÄÚÈÝ
		 	getMethod.releaseConnection();
			return "0";
	}
	public static void main(String[] args) {
		try {
			System.out.println(send("http://127.0.0.1:8080/kw_gjj/review.do?method=autoPrint"));
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
