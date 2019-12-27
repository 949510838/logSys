/**
 * 
 */
package com.logSys.util;

import java.io.FileOutputStream;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
* @Title: sendMail
* @Description: 
* @author: zhang
* @date 2019年12月16日 下午7:40:57
*/
public class sendMail {

	private static String mailFrom = null;// 指明邮件的发件人
	private static String password_mailFrom = null;// 指明邮件的发件人授权码
	private static String mailTo = null;	// 指明邮件的收件人
	private static String mailTittle = null;// 邮件的标题
	private static String mailText = null;	// 邮件的文本内容
	private static String mail_host = null;	// 邮件的服务器域名
	private static String attachedFile = null; //附件的路径
	
	public static void main(String[] args) throws Exception {
		//sendTextMail();
		sendAttachedMail();
	}
 
	/**
	 * @Method: sendTextMail
	 * @Description: 创建一封只包含文本的邮件
	 */
	public static void sendTextMail() throws Exception {
		mailFrom = "949510838@qq.com";
		password_mailFrom = "pkylukyejjgzbchf";
		mailTo = "949510838@qq.com";
		mailTittle = "节日快乐2！";
		mailText = "这是一个简单的邮件1";
		mail_host = "smtp.qq.com";
		
		Properties prop = new Properties();
		prop.setProperty("mail.host", mail_host);
		prop.setProperty("mail.transport.protocol", "smtp");
		prop.setProperty("mail.smtp.auth", "true");
 
		// 使用JavaMail发送邮件的5个步骤
 
		// 1、创建session
		Session session = Session.getInstance(prop);
		// 开启Session的debug模式，这样就可以查看到程序发送Email的运行状态
		session.setDebug(true);
		// 2、通过session得到transport对象
		Transport ts = session.getTransport();
		// 3、使用邮箱的用户名和密码连上邮件服务器，发送邮件时，发件人需要提交邮箱的用户名和密码给smtp服务器，用户名和密码都通过验证之后才能够正常发送邮件给收件人。
		ts.connect(mail_host,mailFrom, password_mailFrom);
		// 4、创建邮件
		// 4.1创建邮件对象
		MimeMessage message = new MimeMessage(session);
		// 4.2指明邮件的发件人
		message.setFrom(new InternetAddress(mailFrom));
		// 4.3指明邮件的收件人，现在发件人和收件人是一样的，那就是自己给自己发
		message.setRecipient(Message.RecipientType.TO, new InternetAddress(mailTo));
		// 4.4邮件的标题
		message.setSubject(mailTittle);
		// 4.5邮件的文本内容
		message.setContent(mailText, "text/html;charset=UTF-8");
		// 返回创建好的邮件对象
		// 5、发送邮件
		ts.sendMessage(message, message.getAllRecipients());
		ts.close();
			
	}
	
	/**
	 * @Method: sendAttachedMail
	 * @Description: 创建一封带附件的邮件
	 */
	public static void sendAttachedMail() throws Exception {
		
		mailFrom = "949510838@qq.com";
		password_mailFrom = "pkylukyejjgzbchf";
		mailTo = "949510838@qq.com";
		mailTittle = "节日快乐2！";
		mailText = "这是一个简单的邮件2";
		mail_host = "smtp.qq.com";
		attachedFile = sendMail.class.getClassLoader().getResource("com/logSys/util/qqyx.txt").getPath().substring(1);
		 
		Properties prop = new Properties();
		prop.setProperty("mail.host", mail_host);// 需要修改
		prop.setProperty("mail.transport.protocol", "smtp");
		prop.setProperty("mail.smtp.auth", "true");
 
		// 使用JavaMail发送邮件的5个步骤
		// 1、创建session
		Session session = Session.getInstance(prop);
		// 开启Session的debug模式，这样就可以查看到程序发送Email的运行状态
		session.setDebug(true);
		// 2、通过session得到transport对象
		Transport ts = session.getTransport();
		// 3、连上邮件服务器，需要发件人提供邮箱的用户名和密码进行验证
		ts.connect(mail_host, mailFrom, password_mailFrom);// 需要修改
		
		// 4、创建邮件
		MimeMessage message = new MimeMessage(session);
		// 设置邮件的基本信息		
		message.setFrom(new InternetAddress(mailFrom));	// 发件人		
		message.setRecipient(Message.RecipientType.TO, new InternetAddress(mailTo));// 收件人
		// 邮件标题
		message.setSubject(mailTittle);
		// 创建邮件正文，为了避免邮件正文中文乱码问题，需要使用charset=UTF-8指明字符编码
		MimeBodyPart text = new MimeBodyPart();
		text.setContent(mailText, "text/html;charset=UTF-8");
		// 创建邮件附件
		MimeBodyPart attach = new MimeBodyPart();
		DataHandler dh = new DataHandler(new FileDataSource(attachedFile));// 需要修改
		attach.setDataHandler(dh);
		attach.setFileName(dh.getName());
		// 创建容器描述数据关系
		MimeMultipart mp = new MimeMultipart();
		mp.addBodyPart(text);
		mp.addBodyPart(attach);
		mp.setSubType("mixed");
		message.setContent(mp);
		message.saveChanges();
		// 将创建的Email写入到F盘存储
		message.writeTo(new FileOutputStream("F:/ImageMail.eml"));// 需要修改
		
		// 5、发送邮件
		ts.sendMessage(message, message.getAllRecipients());
		ts.close();

		
	}


}
