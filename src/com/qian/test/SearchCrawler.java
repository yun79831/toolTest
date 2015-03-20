package com.qian.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchCrawler implements Runnable{
	//��Ų������޸ĵ�url
	private HashMap<String,ArrayList<String>> disallowListCache =new HashMap<String, ArrayList<String>>();
	//��Ŵ����url
	ArrayList<String> errorList=new ArrayList<String>();
	//����������Ľ��
	ArrayList<String> result=new ArrayList<String>();
	String StartUrl;//��ʼ���������
	int maxUrl;//������url��
	String searchString;//Ҫ�������ַ���
	boolean caseSensitive=false;//�Ƿ����ִ�Сд
	boolean limitHost=false;//�Ƿ�����������������
	public SearchCrawler(String startUrl, int maxUrl, String searchString) {
		StartUrl = startUrl;
		this.maxUrl = maxUrl;
		this.searchString = searchString;
	}
	public ArrayList<String> getResult(){
		return result;
	}
	//���url�ĸ�ʽ(��ʱֻ֧��http��ͷ��url)
	private URL verifyUrl(String url){
		if(!url.toLowerCase().startsWith("http://")){
			return null;
		}
		URL verifyUrl=null;
		try {
			verifyUrl=new URL(url);
		} catch (MalformedURLException e) {
			return null;
		}
		return verifyUrl;
	}
	//����url�Ƿ���������ʵ�url
	private boolean isRoBotAllowed(URL urlTocheck){
		//��ȡ����url������
		String host=urlTocheck.getHost().toLowerCase();
		System.out.println("����ip"+host);
		//��ȡ����������������url����
		ArrayList<String> disallowList=disallowListCache.get(host);
		
		//�����û�л��������ز�����
		if(disallowList==null){
			disallowList=new ArrayList<String>();
			try {
				URL robotsFileUrl=new URL("http://"+host+"/robots.txt");
				BufferedReader reader=new BufferedReader(new InputStreamReader(robotsFileUrl.openStream()));
				//��robot�ļ���������������ʵ�·���б�
				String line;
				while((line=reader.readLine())!=null){
					//�Ƿ����"Disallow:"
					if(line.indexOf("Disallow:")==0){
						//��ȡ���дַ��ʵ�·��
						String disallowPath=line.substring("Disallow:".length());
						//����Ƿ���ע��
						int commentIndex=disallowPath.indexOf("#");
						if(commentIndex!=-1){
							disallowPath=disallowPath.substring(0,commentIndex);
						}
						disallowPath=disallowPath.trim();
						disallowList.add(disallowPath);
					}
				}
				//�����������������ʵ�·��
				disallowListCache.put(host, disallowList);
			} catch (Exception e) {
				return true;//webվ��Ŀ¼����û��robots.txt�ļ�������ֵ
			}
		}
		String file=urlTocheck.getFile();
		System.out.println("�ļ�getFile()="+file);
		for(int i=0;i<disallowList.size();i++){
			String disallow=disallowList.get(i);
			if(!"".equals(disallow)&&disallow!=null){
				if(file.startsWith(disallow)){
					return false;
				}
			}
		}
		return true;
	}
	//����ҳ��
	private String downloadPage(URL pageUrl){
		try {
			BufferedReader reader=new BufferedReader(new InputStreamReader(pageUrl.openStream()));
			String line;
			StringBuffer pagebuffer=new StringBuffer();
			while((line=reader.readLine())!=null){
				pagebuffer.append(line);
			}
			return pagebuffer.toString();
		} catch (Exception e) {
			return null;
		}
	}
	//��url��ȥ��"www"
	private String removeWwwFromUrl(String url){
		int index=url.indexOf(";//www");
		if(index!=-1){
			return url.substring(0,index+3)+url.substring(index+7);
		}
		return url;
	}
	//����ҳ�沢�ҳ����ӷ���ҳ���ϵ�����
	private ArrayList<String> retrieveLinks(URL pageUrl,String pageContents,HashSet crawledList,boolean limitHost){
		//��������ʽ�������ӵ�ƥ��ģʽ
		Pattern p=Pattern.compile("<a\\s+href\\s*=\\s*\"?(.*?)[\"|>]>",Pattern.CASE_INSENSITIVE);
		Matcher m=p.matcher(pageContents);
		ArrayList<String> linkList=new ArrayList<String>();
		while(m.find()){
			String link=m.group().trim();
			Pattern pKey = Pattern.compile("href=\"([^\"]*)", 2 | Pattern.DOTALL); 
			Matcher mKey = pKey.matcher(link); 
			if(mKey.find()){
				link=mKey.group().trim().substring(mKey.group().trim().indexOf('"')+1);
			}else{
				continue;
			}
			if(link.length()<1){
				continue;
			}
			//����������ҳ��������
			if(link.charAt(0)=='#'){
				continue;
			}
			//�����ʼ���ͷ
			if(link.indexOf("mailto:")!=-1){
				continue;
			}
			if(link.indexOf("javascript")!=-1){
				continue;
			}
			if(link.indexOf("://")==-1){
				if(link.charAt(0)=='/'){//������Ե�ַ
					link="http://"+pageUrl.getHost()+":"+pageUrl.getPort()+link;
				}else{
					String file=pageUrl.getFile();
					if(file.indexOf('/')==-1){//������Ե�ַ
						link="http://"+pageUrl.getHost()+":"+pageUrl.getPort()+"/"+link;
					}else{
						String path=file.substring(0, file.lastIndexOf('/')+1);
						link="http://"+pageUrl.getHost()+":"+pageUrl.getPort()+path+link;
					}
				}
			}
			int index=link.indexOf('#');
			if(index!=-1){
				link=link.substring(0,index);
			}
			link=removeWwwFromUrl(link);
			URL verifiedlink=verifyUrl(link);
			if(verifiedlink==null){
				continue;
			}
			//�����޶��������ų���Щ������������URL
			if(limitHost&&!pageUrl.getHost().toLowerCase().equals(verifiedlink.getHost().toLowerCase())){
				continue;
			}
			//������Щ�Ѿ����������
			if(crawledList.contains(link)){
				continue;
			}
			linkList.add(link);
		}
		return linkList;
	}
	//����webҳ������ݣ��жϸ�ҳ����û��ָ�� �������ַ���
	private boolean searchStringMatches(String pagecontens,String searchString,boolean caseSensitive){
		String searchContents=pagecontens;
		if(!caseSensitive){
			searchContents=pagecontens.toLowerCase();
		}
		Pattern p=Pattern.compile("[\\s]+");
		String[] terms=p.split(searchString);
		for(int i=0;i<terms.length;i++){
			if(caseSensitive){
				if(searchContents.indexOf(terms[i])==-1){
					return false;
				}
			}else{
				if(searchContents.indexOf(terms[i].toLowerCase())==-1){
					return false;
				}
			}
		}
		return true;
	}
	//ִ��ʵ�ʵ���������
	public ArrayList<String> crawl(String starUrl,int maxUrls,String searchString,boolean limithost,boolean caseSensitive){
		System.out.println("�������ַ�����"+searchString);
		HashSet<String> crawledList=new HashSet<String>();
		LinkedHashSet<String> toCrawlList=new LinkedHashSet<String>();
		if(maxUrls<1){
			errorList.add("Invalid Max URLs value.");
			System.out.println("Invalid Max URLs value.");
		}
		if(searchString.length()<1){
			errorList.add("Missing Search String");
			System.out.println("Missing Search String");
		}
		if(errorList.size()>0){
			System.out.println("error");
			return errorList;
		}
		//�ӿ�ʼurl���Ƴ�wwww
		starUrl=removeWwwFromUrl(starUrl);
		toCrawlList.add(starUrl);
		while(toCrawlList.size()>0){
			if(maxUrl!=-1){
				if (crawledList.size() == maxUrls) {  
			         break;  
			    }  
			}
			String url=toCrawlList.iterator().next();
			toCrawlList.remove(url);
			URL verifiedUrl=verifyUrl(url);
			if(!isRoBotAllowed(verifiedUrl)){
				continue;
			}
			//�����Ѿ������url��crawledList
			crawledList.add(url);
			String pageContents=downloadPage(verifiedUrl);
			if(pageContents!=null && pageContents.length()>0){
				//��ҳ���л�ȡ��Ч������
				ArrayList<String> links=retrieveLinks(verifiedUrl, pageContents, crawledList, limithost);
				toCrawlList.addAll(links);
				if(searchStringMatches(pageContents, searchString, caseSensitive)){
					result.add(url);
					System.out.println(url);
				}
			}
		}
		return result;
	}
	public void run(){//���������߳�  
        
	       crawl(StartUrl,maxUrl, searchString,limitHost,caseSensitive);  
	}
	//���Ժ���
	public static void main(String[] args) {
	    SearchCrawler crawler = new SearchCrawler("http://news.cjn.cn/",100,"�人");  
	    Thread  search=new Thread(crawler);  
	    System.out.println("Start searching...");  
	    System.out.println("result:");  
	    search.start();  
	}
}
