package com.qian.mail;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 发送邮件实体
 * @author qian
 *
 */
public class MailSenderInfo {
	 
    private String mailServerHost;//发送邮件的服务器的IP和端口        
    private String mailServerPort = "25";//邮件发送者的地址    
    private String fromAddress;//邮件发送者的地址     
    private List<String> toAddressList=new ArrayList<String>();//多个邮件接收人
    // 登陆邮件发送服务器的用户名和密码    
    private String userName;    
    private String password;    
    private boolean validate = false;//是否需要身份验证    
    private String subject;//邮件主题    
    private String content;//邮件的文本内容    
    private String[] attachFileNames;//邮件附件的文件名      
    /**   
      * 获得邮件会话属性   
      */    
    public Properties getProperties(){    
      Properties p = new Properties();    
      p.put("mail.smtp.host", this.mailServerHost);    
      p.put("mail.smtp.port", this.mailServerPort);    
      p.put("mail.smtp.auth", validate ? "true" : "false");    
      return p;    
    }   
    /**
     * 设置发送账户
     * @param name  用户名
     * @param password 密码
     */
    public void setUser(String name,String password){
    	this.userName=name;
    	this.fromAddress=name;
    	this.password=password;
    }

    /**
     * 邮箱类型
     * @param mailServerHost 服务器名称
     * @param mailServerPort 服务器端口号
     * <a href="http://wenku.baidu.com/link?url=eItaMSrp3WFGSEdXMpOi324ah6EhXG3b3CP01GFmmAWu03MduGZAnvmSlGzSsApueRHE2W5kxep9vhpNLouw_fjNG6iH-kqFJ0GogdQT-hO" >邮箱服务器列表</a>
     */
    public void setMailService(String mailServerHost,String mailServerPort ){
    	this.mailServerHost=mailServerHost;
    	this.mailServerPort=mailServerPort;
    	
    }
    
    
    public String getMailServerHost() {    
      return mailServerHost;    
    }    
    public void setMailServerHost(String mailServerHost) {    
      this.mailServerHost = mailServerHost;    
    }   
    public String getMailServerPort() {    
      return mailServerPort;    
    }   
    public void setMailServerPort(String mailServerPort) {    
      this.mailServerPort = mailServerPort;    
    }   
    public boolean isValidate() {    
      return validate;    
    }   
    public void setValidate(boolean validate) {    
      this.validate = validate;    
    }   
    public String[] getAttachFileNames() {    
      return attachFileNames;    
    }   
    public void setAttachFileNames(String[] fileNames) {    
      this.attachFileNames = fileNames;    
    }   
    public String getFromAddress() {    
      return fromAddress;    
    }    
    public void setFromAddress(String fromAddress) {    
      this.fromAddress = fromAddress;    
    }   
    public String getPassword() {    
      return password;    
    }   
    public void setPassword(String password) {    
      this.password = password;    
    }   
    
    public String getUserName() {    
      return userName;    
    }   
    public void setUserName(String userName) {    
      this.userName = userName;    
    }   
    public String getSubject() {    
      return subject;    
    }   
    public void setSubject(String subject) {    
      this.subject = subject;    
    }   
    public String getContent() {    
      return content;    
    }   
    public void setContent(String textContent) {    
      this.content = textContent;    
    }
	public List<String> getToAddressList() {
		return toAddressList;
	}
	public void addAddress(String  address) {
		if(this.toAddressList==null)
			this.toAddressList=new ArrayList<String>();
		this.toAddressList.add(address);
	}    
}

