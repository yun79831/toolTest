package com.qian.test;

import com.qian.mail.MailContext;
import com.qian.mail.MailSenderInfo;
import com.qian.mail.SimpleMailSender;

public class TestMail {
	
public static void main(String[] args) {
	MailSenderInfo smail=new MailSenderInfo();
	 MailSenderInfo mailInfo = new MailSenderInfo();    
	 mailInfo.setMailService("smtp.163.com", "25");
	    mailInfo.setValidate(true);  
	    mailInfo.setUser("", "");//������������    
	    mailInfo.addAddress("");  //�ռ�������    �ɶ�ε��ô˷������ж��˷���  
	    mailInfo.setSubject("java�����ʼ�����-����QQ�ǿ���Q����");    
	    mailInfo.setContent("<table><tr><td>����QQ�ǿ���Q���ǵ�һ��</td><td>�ڶ���</td></tr></table>");//���ʹ���html��ʽ���ʼ�   
	    //�������Ҫ�������ʼ�   
	    SimpleMailSender sms = new SimpleMailSender();   
	    for (int i = 0; i < 1000; i++) {
	    	sms.sendMail(mailInfo,MailContext.HTML);//���������ʽ
		}
	    	System.exit(0);
}
}
