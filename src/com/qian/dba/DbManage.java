package com.qian.dba;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;


public class DbManage {
	 Connection con;
	 PreparedStatement ps;
	 ResultSet rs;
	 
	 public DbManage() throws IOException, ClassNotFoundException, SQLException{
		InputStream is= DbManage.class.getResourceAsStream("/config/database/jdbc.properties");
		Properties p = new Properties();
		p.load(is);
		String className = p.getProperty("kwep.driverClassName");
		String url = p.getProperty("jdbc.url");
		String user = p.getProperty("jdbc.username");
		String pwd = p.getProperty("jdbc.password");
		Class.forName(className);
		this.con=DriverManager.getConnection(url, user, pwd);
	 }
	
	 public Connection getConn(){
		 InputStream is= DbManage.class.getResourceAsStream("/config/database/jdbc.properties");
			try {
				Properties p = new Properties();
				p.load(is);
				String className = p.getProperty("kwep.driverClassName");
				String url = p.getProperty("jdbc.url");
				String user = p.getProperty("jdbc.username");
				String pwd = p.getProperty("jdbc.password");
				Class.forName(className);
				this.con=DriverManager.getConnection(url, user, pwd);
			} catch (IOException e) {
				System.out.println("not find file");
			} catch (ClassNotFoundException e) {
				System.out.println("---ClassNotFoundException---");
			} catch (SQLException e) {
				System.out.println("-----SQLException-----");
			}
			return con;
	 }
	 
	 
	 
	 
	 
}
