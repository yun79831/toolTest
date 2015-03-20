package com.qian.mail;
import java.util.Date;    
import java.util.Properties;   
import javax.mail.Address;    
import javax.mail.AuthenticationFailedException;
import javax.mail.BodyPart;    
import javax.mail.Message;    
import javax.mail.MessagingException;    
import javax.mail.Multipart;    
import javax.mail.Session;    
import javax.mail.Transport;    
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;    
import javax.mail.internet.MimeBodyPart;    
import javax.mail.internet.MimeMessage;    
import javax.mail.internet.MimeMultipart; 

import com.sun.mail.smtp.SMTPSendFailedException;

/**
 * 发送消息操作类
 * @author Administrator
 *
 */
public class SimpleMailSender {
	
	
	/**
	 * 发送邮件
	 * @param 消息实体
	 * @param 消息类型   TXT，HTML
	 * @return  是否发送成功
	 */
	public boolean sendMail(MailSenderInfo mailInfo,MailContext type){
	    MyAuthenticator authenticator = null;    
	      Properties pro = mailInfo.getProperties();   
	      if (mailInfo.isValidate()) {    
	      // 如果需要身份认证，则创建一个密码验证器    
	        authenticator = new MyAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());    
	      }   
	      // 根据邮件会话属性和密码验证器构造一个发送邮件的session    
	      Session sendMailSession = Session.getDefaultInstance(pro,authenticator);   
	      Message mailMessage = new MimeMessage(sendMailSession);   //消息体 
	      // 创建邮件发送者地址    
			try {
				Address from = new InternetAddress(mailInfo.getFromAddress());    
				  // 设置邮件消息的发送者    
				   mailMessage.setFrom(from);  
				      int len=mailInfo.getToAddressList().size();
				      Address[] toAddresslist=new InternetAddress[len];
				      for(int i=0;i<len;i++){
				    	  toAddresslist[i]=new InternetAddress(mailInfo.getToAddressList().get(i));
				      }
				      // 创建邮件的接收者地址，并设置到邮件消息中        
				      
				      if(len>0){
				    		  mailMessage.setRecipients(Message.RecipientType.TO,toAddresslist);   
				      }
				      else{
				    	  System.out.println("没有收件人 ");
				    	  return false;
				      }
				      
				      // 设置邮件消息的主题    
				      mailMessage.setSubject(mailInfo.getSubject());    
				      // 设置邮件消息发送的时间    
				      mailMessage.setSentDate(new Date());    
				      // 设置邮件消息的主要内容    
				      String mailContent = mailInfo.getContent();    
				
				      if(type==MailContext.TXT)
				    	  mailMessage.setText(mailContent);  
				      if(type==MailContext.HTML){
				    	  // MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象    
					      Multipart mainPart = new MimeMultipart();    
					      // 创建一个包含HTML内容的MimeBodyPart    
					      BodyPart html = new MimeBodyPart();    
					      // 设置HTML内容    
					      html.setContent(mailContent, "text/html; charset=utf-8");    
					      mainPart.addBodyPart(html);    
					      // 将MiniMultipart对象设置为邮件内容    
					      mailMessage.setContent(mainPart);    
				      }
				      
			} catch (AddressException e) {
			System.out.println("用户名错误");
			return false;
			} catch (MessagingException e) {
			System.out.println("消息内容异常");
			return false;
			}
			
			try {//发送邮件
				Transport.send(mailMessage);
			} catch (MessagingException e) {
			System.out.println("发送失败");
			 return false;
			}
	      
		return true;
	}
}
