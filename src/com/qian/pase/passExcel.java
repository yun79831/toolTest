package com.qian.pase;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;


/**
 * 功能：拆分excel，将excel中的多个sheet拆分为多个excel文件下载
 * 注释： 	如果excel中的sheet表的名字没有变，则都会重新生成新文件覆盖缓存文件
 * 		如果excel中的sheet表的名字变化(即excel每重新生成一次)，则删除存放该excel的sheet表的整个文件夹
 * @author qian
 *
 */
public class passExcel {
	static int count=0;
	/**
	 * 
	 * @param formpath 要拆分excel的具体地址（包含文件名称）
	 * @param index 要拆分的sheet的下标
	 * @param toPath 查分完成后的保存地址（不包含文件名）
	 */
	public static void paseXls(String formpath,int index,String toPath) {
		String realSavePath=null;
		String saveFileName=null;
		File formFile=new File(formpath);
		String formFileName=formFile.getName().substring(0,formFile.getName().lastIndexOf("."));
		if(formFile.exists()){
			Workbook oldBook;
			try {
				oldBook = Workbook.getWorkbook(formFile);
				if(oldBook!=null){
					//判断此路径线面文件夹是否存在不存在则创建
					String savepath=toPath+formFileName+"\\";
					File fileExists=new File(savepath);
					if(!fileExists.exists()){
						fileExists.mkdirs();
					}
					saveFileName=oldBook.getSheetNames()[index]; 
					realSavePath=savepath+formFileName+"_"+saveFileName+"_"+index+".xls";
				}
				
				File filetemp=new File(realSavePath);
				WritableWorkbook newBook=Workbook.createWorkbook(filetemp,oldBook);
				if(newBook!=null){
					for(int j=newBook.getSheets().length-1;j>=0;j++){
						if(index!=j){
							newBook.removeSheet(j);
						}
					}
				}
				newBook.write();
				newBook.close();

			} catch (BiffException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}catch (WriteException e) {
				e.printStackTrace();
			}
			
		}else{
			System.out.println(formpath+"不存在");
		}
		//下载代码
	}
	
	  public static int findFiles(String baseDirName) {   
	        /**  
	         * 算法简述：  
	         * 从某个给定的需查找的文件夹出发，搜索该文件夹的所有子文件夹及文件，  
	         * 若为文件，则进行匹配，匹配成功则加入结果集，若为子文件夹，则进队列。  
	         * 队列不空，重复上述操作，队列为空，程序结束，返回结果。  
	         */  
	        String tempName = null;  
	        //判断目录是否存在   
	        File baseDir = new File(baseDirName);   
	        if (!baseDir.exists() || !baseDir.isDirectory()){   
	            System.out.println("文件查找失败：" + baseDirName + "不是一个目录！");   
	        } else {   
	            String[] filelist = baseDir.list();   
	            for (int i = 0; i < filelist.length; i++) {   
	                File readfile = new File(baseDirName + "\\" + filelist[i]);   
	                if(!readfile.isDirectory()){
	                	count++;
	                }else{
	                	findFiles(baseDirName + "\\" + filelist[i]);
	                }
	            }   
	        }
	       return count;
	    }   
	  //删除指定目录下面的所有文件和文件夹
	  public static void delFiles(String baseDirName) {   
	        /**  
	         * 算法简述：  
	         * 从某个给定的需查找的文件夹出发，搜索该文件夹的所有子文件夹及文件，  
	         * 若为文件，则进行匹配，匹配成功则加入结果集，若为子文件夹，则进队列。  
	         * 队列不空，重复上述操作，队列为空，程序结束，返回结果。  
	         */  
	        //判断目录是否存在   
	        File baseDir = new File(baseDirName);   
	        if (!baseDir.exists() || !baseDir.isDirectory()){   
	            System.out.println("文件查找失败：" + baseDirName + "不是一个目录！");   
	        } else {   
	            String[] filelist = baseDir.list();   
	            for (int i = 0; i < filelist.length; i++) {   
	                File readfile = new File(baseDirName + "\\" + filelist[i]);   
	                if(!readfile.isDirectory()){
	                	readfile.delete();
	                }else{
	                	delFiles(baseDirName + "\\" + filelist[i]);
	                	readfile.delete();
	                }
	            }   
	        }
	    }   
	public static void main(String[] args) {
		paseXls("e:\\excel\\2013.xls",2,"e:\\excel\\");

	}
}
