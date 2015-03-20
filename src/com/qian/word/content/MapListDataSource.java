package com.qian.word.content;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.aspose.words.IMailMergeDataSource;

/**
 * 封装循环实体，实体用map进行分装
 * @author 钱王鹏
 *--@deprecated  没有直接封装实体有用(已弃用)(已做修改)
 */
public class MapListDataSource implements IMailMergeDataSource  {
	private List<Map<String, Object>> dataList;  
    
    private int index;  
      
    //word模板中的
    private String tableName = null;  
      
    /** 
     * @param dataList 数据集 
     * @param tableName 与模板中的Name对应 
     */  
    public MapListDataSource(List<Map<String, Object>> dataList, String tableName) {  
        this.dataList = dataList;  
        this.tableName = tableName;  
        index = -1;  
    }  
      
    /** 
     * @param data 单个数据集 
     * @param tableName 与模板中的Name对应 
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
     * 获取结果集总数 
     * @return 
     */  
    private int getCount() {  
        return this.dataList.size();  
    }  
      

     
    public String getTableName() throws Exception {  
        return this.tableName;  
    }  
  
    /** 
     * 实现接口 
     * 获取当前index指向数据行的数据 
     * 将数据存入args数组中即可 
     * @return ***返回false则不绑定数据*** 
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
     * 实现接口 
     * 判断是否还有下一条记录 
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