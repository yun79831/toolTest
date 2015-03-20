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
	//存放不允许修改的url
	private HashMap<String,ArrayList<String>> disallowListCache =new HashMap<String, ArrayList<String>>();
	//存放错误的url
	ArrayList<String> errorList=new ArrayList<String>();
	//存放搜索到的结果
	ArrayList<String> result=new ArrayList<String>();
	String StartUrl;//开始搜索的起点
	int maxUrl;//最大处理的url数
	String searchString;//要搜索的字符串
	boolean caseSensitive=false;//是否区分大小写
	boolean limitHost=false;//是否限制在主机内搜索
	public SearchCrawler(String startUrl, int maxUrl, String searchString) {
		StartUrl = startUrl;
		this.maxUrl = maxUrl;
		this.searchString = searchString;
	}
	public ArrayList<String> getResult(){
		return result;
	}
	//检测url的格式(暂时只支持http开头的url)
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
	//检测该url是否是允许访问的url
	private boolean isRoBotAllowed(URL urlTocheck){
		//获取给出url的主机
		String host=urlTocheck.getHost().toLowerCase();
		System.out.println("主机ip"+host);
		//获取主机不允许搜索的url缓存
		ArrayList<String> disallowList=disallowListCache.get(host);
		
		//如果还没有缓存则下载病缓存
		if(disallowList==null){
			disallowList=new ArrayList<String>();
			try {
				URL robotsFileUrl=new URL("http://"+host+"/robots.txt");
				BufferedReader reader=new BufferedReader(new InputStreamReader(robotsFileUrl.openStream()));
				//读robot文件，创建不允许访问的路径列表
				String line;
				while((line=reader.readLine())!=null){
					//是否包含"Disallow:"
					if(line.indexOf("Disallow:")==0){
						//获取不孕粗访问的路径
						String disallowPath=line.substring("Disallow:".length());
						//检测是否有注释
						int commentIndex=disallowPath.indexOf("#");
						if(commentIndex!=-1){
							disallowPath=disallowPath.substring(0,commentIndex);
						}
						disallowPath=disallowPath.trim();
						disallowList.add(disallowPath);
					}
				}
				//缓存此主机不允许访问的路径
				disallowListCache.put(host, disallowList);
			} catch (Exception e) {
				return true;//web站点目录下面没有robots.txt文件，返回值
			}
		}
		String file=urlTocheck.getFile();
		System.out.println("文件getFile()="+file);
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
	//下载页面
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
	//从url中去掉"www"
	private String removeWwwFromUrl(String url){
		int index=url.indexOf(";//www");
		if(index!=-1){
			return url.substring(0,index+3)+url.substring(index+7);
		}
		return url;
	}
	//解析页面并找出链接返回页面上的链接
	private ArrayList<String> retrieveLinks(URL pageUrl,String pageContents,HashSet crawledList,boolean limitHost){
		//用正则表达式编译链接的匹配模式
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
			//跳过链到本页面内链接
			if(link.charAt(0)=='#'){
				continue;
			}
			//发送邮件开头
			if(link.indexOf("mailto:")!=-1){
				continue;
			}
			if(link.indexOf("javascript")!=-1){
				continue;
			}
			if(link.indexOf("://")==-1){
				if(link.charAt(0)=='/'){//处理绝对地址
					link="http://"+pageUrl.getHost()+":"+pageUrl.getPort()+link;
				}else{
					String file=pageUrl.getFile();
					if(file.indexOf('/')==-1){//处理相对地址
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
			//若果限定主机，排除那些不符合条件的URL
			if(limitHost&&!pageUrl.getHost().toLowerCase().equals(verifiedlink.getHost().toLowerCase())){
				continue;
			}
			//跳过那些已经处理的链接
			if(crawledList.contains(link)){
				continue;
			}
			linkList.add(link);
		}
		return linkList;
	}
	//搜索web页面的内容，判断该页面有没有指定 的搜索字符串
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
	//执行实际的搜索操作
	public ArrayList<String> crawl(String starUrl,int maxUrls,String searchString,boolean limithost,boolean caseSensitive){
		System.out.println("搜索的字符串："+searchString);
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
		//从开始url中移除wwww
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
			//增加已经处理的url到crawledList
			crawledList.add(url);
			String pageContents=downloadPage(verifiedUrl);
			if(pageContents!=null && pageContents.length()>0){
				//从页面中获取有效的链接
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
	public void run(){//启动搜索线程  
        
	       crawl(StartUrl,maxUrl, searchString,limitHost,caseSensitive);  
	}
	//测试函数
	public static void main(String[] args) {
	    SearchCrawler crawler = new SearchCrawler("http://news.cjn.cn/",100,"武汉");  
	    Thread  search=new Thread(crawler);  
	    System.out.println("Start searching...");  
	    System.out.println("result:");  
	    search.start();  
	}
}
