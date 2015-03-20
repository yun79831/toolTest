package com.qian.dba;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class BaseDao {
	protected Connection con;
	protected ResultSet rs;
	protected PreparedStatement pst;
	protected Statement st;
	
	/**
	 * 加载数据库连接
	 */
    public void getConn(){
   	 InputStream is= DbManage.class.getResourceAsStream("/config/database/jdbc.properties");
		try {
			Properties p = new Properties();
			p.load(is);
			String className = p.getProperty("jdbc.driverClassName");
			String url = p.getProperty("jdbc.url");
			String user = p.getProperty("jdbc.username");
			String pwd = p.getProperty("jdbc.password");
			Class.forName(className);
			this.con=DriverManager.getConnection(url, user, pwd);
			 st=con.createStatement();
		} catch (IOException e) {
			System.out.println("not find file");
		} catch (ClassNotFoundException e) {
			System.out.println("---ClassNotFoundException---");
		} catch (SQLException e) {
			System.out.println("-----SQLException-----");
		}
    }
    /**
     * 关闭数据库连接
     */
    public void AllClose(){
    	try {
			if(rs!=null){
				rs.close();
				rs=null;
			}
			if(pst!=null){
				pst.close();
				pst=null;
			}
			if(st!=null){
				st.close();
				st=null;
			}
			if(con!=null){
				con.close();
				con=null;
			}
		} catch (SQLException e) {
			System.out.println("关闭失败");
		}
    }
    /**
     * 查询个数
     */
    public int Count(String sql,String err){
 	   this.getConn();
 	   int count=0;
 	   try {
		rs=st.executeQuery(sql);
		   if (rs.next()) {
			  count=rs.getInt(1);
		}
	} catch (SQLException e) {
		System.out.println(err+e.toString());
	}finally{
		this.AllClose();
	}
	return count;
    }
    
    /**
     * 执行修改
     * @return
     */
    public int update(String sql,String err){
    	this.getConn();
    	int b=0;
    	try {
			b=st.executeUpdate(sql);
		} catch (SQLException e) {
			System.out.println(err+e.toString());
		}finally{
			this.AllClose();
		}
    	 
    	return b;
    }
    
    
}
