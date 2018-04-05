package com.advantage.reporting;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

import org.testng.annotations.*;

import com.advantage.datastructures.testLink.SendEmailDetails;

/**
 * This class is for e-mail functions.
 */
public class Email {
	public void sendEmail(SendEmailDetails info)
	{
		try
		{
			// Get system properties
			Properties props = System.getProperties();

			// Setup mail server
			props.put("mail.smtp.host", info.sSMPT_Server);
			//props.put("mail.user", user);
			//props.put("mail.password", pass);
			props.setProperty("mail.smtp.port", "25");
			props.put("mail.smtp.auth", "true");
			// Get the default Session object.
			//Session session = Session.getDefaultInstance(properties);

			// Get session
			//Session session = Session.getInstance(props, null);
			Session session = Session.getDefaultInstance(props,
					new javax.mail.Authenticator() {

					protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication("", "");
					}
					});

					

			// Define message
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(info.sFromEmailAddress));

			// add the recipients
			for (int i = 0; i < info.sToEmailAddresses.length; i++)
			{
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(info.sToEmailAddresses[i]));
			}

			// subject
			message.setSubject(info.sSubject);

			// create the message part
			MimeBodyPart messageBodyPart = new MimeBodyPart();

			// fill message
			messageBodyPart.setText(info.sMessageText);

			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);

			// Part two is attachments
			for (int i = 0; i < info.sFileAttachments.length; i++)
			{
				messageBodyPart = new MimeBodyPart();
				DataSource source = new FileDataSource(info.sFileAttachments[i]);
				messageBodyPart.setDataHandler(new DataHandler(source));
				// Use the filename only (exclude the path)
				String[] filenames = info.sFileAttachments[i].split(info.sPathSeparator);
				messageBodyPart.setFileName(filenames[filenames.length - 1]);
				multipart.addBodyPart(messageBodyPart);
			}

			// Put parts in message
			message.setContent(multipart);

			// Send the message
			Transport transport = session.getTransport("smtp");
			transport.connect();
			transport.sendMessage(message, message.getAllRecipients());
		}
		catch (Exception ex)
		{
			Logs.log.error("Sending e-mail failed due to exception.  Exception Details:" + info.sNewLine + ex);
		}
	}

	/**
	 * Method called by testNG to test sendEmail
	 */
	@Test
	public static void unitTest()
	{
		Logs.initializeLoggers();
		Email results = new Email();
		// No Attachment
		SendEmailDetails details = new SendEmailDetails("exch2007.univeris.com", "emailTest@univeris.com",
				new String[] { "dneill@univeris.com" }, "Unit Test sendEmail 1/3",
				"E-mail is working without an attachment", new String[] {});
		results.sendEmail(details);

		// 1 Attachment
		SendEmailDetails details2 = new SendEmailDetails("exch2007.univeris.com", "emailTest@univeris.com",
				new String[] { "dneill@univeris.com" }, "Unit Test sendEmail 2/3",
				"E-mail is working with an attachment", new String[] { "csvTest.csv" });
		results.sendEmail(details2);

		// 2 Attachments
		SendEmailDetails details3 = new SendEmailDetails("exch2007.univeris.com", "emailTest@univeris.com",
				new String[] { "dneill@univeris.com" }, "Unit Test sendEmail 3/3",
				"E-mail is working with an attachment", new String[] { "csvTest.csv", "xmlTest.xml" });
		results.sendEmail(details3);
	}
}
