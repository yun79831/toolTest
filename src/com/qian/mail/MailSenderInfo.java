package com.qian.mail;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * �����ʼ�ʵ��
 * @author qian
 *
 */
public class MailSenderInfo {
	 
    private String mailServerHost;//�����ʼ��ķ�������IP�Ͷ˿�        
    private String mailServerPort = "25";//�ʼ������ߵĵ�ַ    
    private String fromAddress;//�ʼ������ߵĵ�ַ     
    private List<String> toAddressList=new ArrayList<String>();//����ʼ�������
    // ��½�ʼ����ͷ��������û���������    
    private String userName;    
    private String password;    
    private boolean validate = false;//�Ƿ���Ҫ�����֤    
    private String subject;//�ʼ�����    
    private String content;//�ʼ����ı�����    
    private String[] attachFileNames;//�ʼ��������ļ���      
    /**   
      * ����ʼ��Ự����   
      */    
    public Properties getProperties(){    
      Properties p = new Properties();    
      p.put("mail.smtp.host", this.mailServerHost);    
      p.put("mail.smtp.port", this.mailServerPort);    
      p.put("mail.smtp.auth", validate ? "true" : "false");    
      return p;    
    }   
    /**
     * ���÷����˻�
     * @param name  �û���
     * @param password ����
     */
    public void setUser(String name,String password){
    	this.userName=name;
    	this.fromAddress=name;
    	this.password=password;
    }

    /**
     * ��������
     * @param mailServerHost ����������
     * @param mailServerPort �������˿ں�
     * <a href="http://wenku.baidu.com/link?url=eItaMSrp3WFGSEdXMpOi324ah6EhXG3b3CP01GFmmAWu03MduGZAnvmSlGzSsApueRHE2W5kxep9vhpNLouw_fjNG6iH-kqFJ0GogdQT-hO" >����������б�</a>
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

