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
 * ������Ϣ������
 * @author Administrator
 *
 */
public class SimpleMailSender {
	
	
	/**
	 * �����ʼ�
	 * @param ��Ϣʵ��
	 * @param ��Ϣ����   TXT��HTML
	 * @return  �Ƿ��ͳɹ�
	 */
	public boolean sendMail(MailSenderInfo mailInfo,MailContext type){
	    MyAuthenticator authenticator = null;    
	      Properties pro = mailInfo.getProperties();   
	      if (mailInfo.isValidate()) {    
	      // �����Ҫ�����֤���򴴽�һ��������֤��    
	        authenticator = new MyAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());    
	      }   
	      // �����ʼ��Ự���Ժ�������֤������һ�������ʼ���session    
	      Session sendMailSession = Session.getDefaultInstance(pro,authenticator);   
	      Message mailMessage = new MimeMessage(sendMailSession);   //��Ϣ�� 
	      // �����ʼ������ߵ�ַ    
			try {
				Address from = new InternetAddress(mailInfo.getFromAddress());    
				  // �����ʼ���Ϣ�ķ�����    
				   mailMessage.setFrom(from);  
				      int len=mailInfo.getToAddressList().size();
				      Address[] toAddresslist=new InternetAddress[len];
				      for(int i=0;i<len;i++){
				    	  toAddresslist[i]=new InternetAddress(mailInfo.getToAddressList().get(i));
				      }
				      // �����ʼ��Ľ����ߵ�ַ�������õ��ʼ���Ϣ��        
				      
				      if(len>0){
				    		  mailMessage.setRecipients(Message.RecipientType.TO,toAddresslist);   
				      }
				      else{
				    	  System.out.println("û���ռ��� ");
				    	  return false;
				      }
				      
				      // �����ʼ���Ϣ������    
				      mailMessage.setSubject(mailInfo.getSubject());    
				      // �����ʼ���Ϣ���͵�ʱ��    
				      mailMessage.setSentDate(new Date());    
				      // �����ʼ���Ϣ����Ҫ����    
				      String mailContent = mailInfo.getContent();    
				
				      if(type==MailContext.TXT)
				    	  mailMessage.setText(mailContent);  
				      if(type==MailContext.HTML){
				    	  // MiniMultipart����һ�������࣬����MimeBodyPart���͵Ķ���    
					      Multipart mainPart = new MimeMultipart();    
					      // ����һ������HTML���ݵ�MimeBodyPart    
					      BodyPart html = new MimeBodyPart();    
					      // ����HTML����    
					      html.setContent(mailContent, "text/html; charset=utf-8");    
					      mainPart.addBodyPart(html);    
					      // ��MiniMultipart��������Ϊ�ʼ�����    
					      mailMessage.setContent(mainPart);    
				      }
				      
			} catch (AddressException e) {
			System.out.println("�û�������");
			return false;
			} catch (MessagingException e) {
			System.out.println("��Ϣ�����쳣");
			return false;
			}
			
			try {//�����ʼ�
				Transport.send(mailMessage);
			} catch (MessagingException e) {
			System.out.println("����ʧ��");
			 return false;
			}
	      
		return true;
	}
}
