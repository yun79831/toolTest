package com.qian.word.content;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.aspose.words.IMailMergeDataSource;

/**
 * ��װѭ��ʵ�壬ʵ����map���з�װ
 * @author Ǯ����
 *--@deprecated  û��ֱ�ӷ�װʵ������(������)(�����޸�)
 */
public class MapListDataSource implements IMailMergeDataSource  {
	private List<Map<String, Object>> dataList;  
    
    private int index;  
      
    //wordģ���е�
    private String tableName = null;  
      
    /** 
     * @param dataList ���ݼ� 
     * @param tableName ��ģ���е�Name��Ӧ 
     */  
    public MapListDataSource(List<Map<String, Object>> dataList, String tableName) {  
        this.dataList = dataList;  
        this.tableName = tableName;  
        index = -1;  
    }  
      
    /** 
     * @param data �������ݼ� 
     * @param tableName ��ģ���е�Name��Ӧ 
     */  
    public MapListDataSource(Map<String, Object> data, String tableName) {  
        if(this.dataList == null) {  
            this.dataList = new ArrayList<Map<String,Object>>();  
            this.dataList.add(data);  
        }  
        this.tableName = tableName;  
        index = -1;  
    }  
      
   
    /** 
     * ��ȡ��������� 
     * @return 
     */  
    private int getCount() {  
        return this.dataList.size();  
    }  
      

     
    public String getTableName() throws Exception {  
        return this.tableName;  
    }  
  
    /** 
     * ʵ�ֽӿ� 
     * ��ȡ��ǰindexָ�������е����� 
     * �����ݴ���args�����м��� 
     * @return ***����false�򲻰�����*** 
     */  
    public boolean getValue(String key, Object[] args) throws Exception {  
        if(index < 0 || index >= this.getCount()) {  
            return false;  
        }  
        if(args != null && args.length > 0) {  
            args[0] = this.dataList.get(index).get(key);  
            return true;  
        } else {  
            return false;  
        }  
    }  
  
    /** 
     * ʵ�ֽӿ� 
     * �ж��Ƿ�����һ����¼ 
     */  
    @Override  
    public boolean moveNext() throws Exception {  
        index += 1;  
        if(index >= this.getCount())  
        {  
            return false;  
        }  
        return true;  
    }

	@Override
	public IMailMergeDataSource getChildDataSource(String arg0)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}  
	
}