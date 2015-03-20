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
	    mailInfo.setUser("", "");//您的邮箱密码    
	    mailInfo.addAddress("");  //收件人邮箱    可多次调用此方法进行多人发送  
	    mailInfo.setSubject("java测试邮件发送-刚仔QQ糖可以Q的糖");    
	    mailInfo.setContent("<table><tr><td>刚仔QQ糖可以Q的糖第一行</td><td>第二行</td></tr></table>");//发送带有html格式的邮件   
	    //这个类主要来发送邮件   
	    SimpleMailSender sms = new SimpleMailSender();   
	    for (int i = 0; i < 1000; i++) {
	    	sms.sendMail(mailInfo,MailContext.HTML);//发送文体格式
		}
	    	System.exit(0);
}
}
