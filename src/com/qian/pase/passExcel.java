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
 * ���ܣ����excel����excel�еĶ��sheet���Ϊ���excel�ļ�����
 * ע�ͣ� 	���excel�е�sheet�������û�б䣬�򶼻������������ļ����ǻ����ļ�
 * 		���excel�е�sheet������ֱ仯(��excelÿ��������һ��)����ɾ����Ÿ�excel��sheet��������ļ���
 * @author qian
 *
 */
public class passExcel {
	static int count=0;
	/**
	 * 
	 * @param formpath Ҫ���excel�ľ����ַ�������ļ����ƣ�
	 * @param index Ҫ��ֵ�sheet���±�
	 * @param toPath �����ɺ�ı����ַ���������ļ�����
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
					//�жϴ�·�������ļ����Ƿ���ڲ������򴴽�
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
			System.out.println(formpath+"������");
		}
		//���ش���
	}
	
	  public static int findFiles(String baseDirName) {   
	        /**  
	         * �㷨������  
	         * ��ĳ������������ҵ��ļ��г������������ļ��е��������ļ��м��ļ���  
	         * ��Ϊ�ļ��������ƥ�䣬ƥ��ɹ��������������Ϊ���ļ��У�������С�  
	         * ���в��գ��ظ���������������Ϊ�գ�������������ؽ����  
	         */  
	        String tempName = null;  
	        //�ж�Ŀ¼�Ƿ����   
	        File baseDir = new File(baseDirName);   
	        if (!baseDir.exists() || !baseDir.isDirectory()){   
	            System.out.println("�ļ�����ʧ�ܣ�" + baseDirName + "����һ��Ŀ¼��");   
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
	  //ɾ��ָ��Ŀ¼����������ļ����ļ���
	  public static void delFiles(String baseDirName) {   
	        /**  
	         * �㷨������  
	         * ��ĳ������������ҵ��ļ��г������������ļ��е��������ļ��м��ļ���  
	         * ��Ϊ�ļ��������ƥ�䣬ƥ��ɹ��������������Ϊ���ļ��У�������С�  
	         * ���в��գ��ظ���������������Ϊ�գ�������������ؽ����  
	         */  
	        //�ж�Ŀ¼�Ƿ����   
	        File baseDir = new File(baseDirName);   
	        if (!baseDir.exists() || !baseDir.isDirectory()){   
	            System.out.println("�ļ�����ʧ�ܣ�" + baseDirName + "����һ��Ŀ¼��");   
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
