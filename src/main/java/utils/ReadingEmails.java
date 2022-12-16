package utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;

import org.testng.Reporter;

import com.sun.tools.sjavac.Log;

import bsh.Console;

public class ReadingEmails {

	Properties properties = null;
	private Session session = null;
	private Store store = null;
	private Folder inbox = null;
	private String result;
	Path currentRelativePath = Paths.get("");
	String s = currentRelativePath.toAbsolutePath().toString();
	String basePath = s + File.separator;

	public Store authenticate(String userName, String password) throws MessagingException {
		properties = new Properties();
		properties.setProperty("mail.host", "imap.gmail.com");
		properties.setProperty("mail.port", "993");
		properties.setProperty("mail.transport.protocol", "imaps");
		session = Session.getInstance(properties, new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(userName, password);
			}
		});
		store = session.getStore("imaps");
		store.connect();
		return store;
	}

	public boolean hasAliasName(String username, String password, String fromName) throws Exception {
		boolean flag = false;
		int count = 0;
		store = authenticate(username, password);
		inbox = store.getFolder("INBOX");
		inbox.open(Folder.READ_WRITE);
		Message messages[] = inbox.search(new FlagTerm(new Flags(Flag.SEEN), false));
		if (messages.length == 0) {
			Thread.sleep(10000);
		} else {
			for (int i = messages.length - 1; i >= 0; i--) {
				Message message = messages[i];
				count++;
				// System.out.println("Subject: " + message.getSubject());
				// System.out.println("From: " + message.getFrom()[0]);
				String fromNames = message.getFrom()[0].toString();
				// System.out.println("To: " + message.getAllRecipients()[0]);
				// System.out.println("Date: " + message.getReceivedDate());
				// System.out.println("Size: " + message.getSize());
				// System.out.println("Flags: " + message.getFlags());
				// System.out.println("ContentType: " + message.getContentType());
				// System.out.println("Body: \n//System.out.printlnail));
				// System.out.println("Has Attachments: " +
				// hasAttachments(message));
				if (fromNames.contains(fromName)) {
					flag = true;
					message.setFlag(Flag.DELETED, true);

					if (message.getContentType().contains("multipart")) {
						MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
						result = getTextFromMimeMultipart(mimeMultipart);
					}

					if (message.isMimeType("text/plain") || message.isMimeType("text/html")) {
						result = message.getContent().toString();
					}
				}
				if (flag == true) {
					inbox.close(true);
					break;
				}
				if (count == 10)
					break;
			}
		}
		return flag;
	}

	public String getMailBodyContent(String userName, String password, String subject) throws Exception {
		boolean flag = false;
		int count = 0;
		store = authenticate(userName, password);
		inbox = store.getFolder("INBOX");
		inbox.open(Folder.READ_WRITE);
		Message messages[] = inbox.search(new FlagTerm(new Flags(Flag.SEEN), false));
		if (messages.length == 0) {
			Thread.sleep(10000);
		} else {
			for (int i = messages.length - 1; i >= 0; i--) {
				Message message = messages[i];
				count++;
				// System.out.println("Subject: " + message.getSubject());
				// System.out.println("From: " + message.getFrom()[0]);
				// System.out.println("To: " + message.getAllRecipients()[0]);
				// System.out.println("Date: " + message.getReceivedDate());
				// System.out.println("Size: " + message.getSize());
				// System.out.println("Flags: " + message.getFlags());
				// System.out.println("ContentType: " + message.getContentType());
				// System.out.println("Body: \n" + getEmailBody(mail));
				// System.out.println("Has Attachments: " + hasAttachments(message));
				if (message.getSubject().contains(subject)) {
					flag = true;
					message.setFlag(Flag.DELETED, true);

					if (message.getContentType().contains("multipart")) {
						MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
						result = getTextFromMimeMultipart(mimeMultipart);
					}

					if (message.isMimeType("text/plain") || message.isMimeType("text/html")) {
						result = message.getContent().toString();
					}
				}
				if (flag == true) {
					inbox.close(true);
					break;
				}
				if (count == 10)
					break;
			}
		}
		return result;
	}

	public String getMailBodyContent(String userName, String password, String subject, int messageCount)
			throws Exception {
		boolean flag = false;
		int count = 0;
		store = authenticate(userName, password);
		inbox = store.getFolder("INBOX");
		inbox.open(Folder.READ_WRITE);
		Message messages[] = inbox.search(new FlagTerm(new Flags(Flag.SEEN), false));
		if (messages.length == 0) {
			Thread.sleep(10000);
		} else {
			for (int i = messages.length - 1; i >= 0; i--) {
				Message message = messages[i];
				count++;
				// System.out.println("Subject: " + message.getSubject());
				// System.out.println("From: " + message.getFrom()[0]);
				// System.out.println("To: " + message.getAllRecipients()[0]);
				// System.out.println("Date: " + message.getReceivedDate());
				// System.out.println("Size: " + message.getSize());
				// System.out.println("Flags: " + message.getFlags());
				// System.out.println("ContentType: " + message.getContentType());
				// System.out.println("Body: \n" + getEmailBody(mail));
				// System.out.println("Has Attachments: " + hasAttachments(message));
				if (message.getSubject().contains(subject)) {
					flag = true;
					message.setFlag(Flag.DELETED, true);

					if (message.getContentType().contains("multipart")) {
						MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
						result = getTextFromMimeMultipart(mimeMultipart);
					}

					if (message.isMimeType("text/plain") || message.isMimeType("text/html")) {
						result = message.getContent().toString();
					}
				}
				if (flag == true) {
					inbox.close(true);
					break;
				}
				if (count == messageCount)
					break;
			}
		}
		return result;
	}

	public String getMailBodyContentByAliasName(String userName, String password, String fromName) throws Exception {
		boolean flag = false;
		int count = 0;
		store = authenticate(userName, password);
		inbox = store.getFolder("INBOX");
		inbox.open(Folder.READ_WRITE);
		Message messages[] = inbox.search(new FlagTerm(new Flags(Flag.SEEN), false));
		if (messages.length == 0) {
			Thread.sleep(10000);
		} else {
			for (int i = messages.length - 1; i >= 0; i--) {
				Message message = messages[i];
				count++;
				System.out.println("Subject: " + message.getSubject());
				System.out.println("From: " + message.getFrom()[0]);
				String fromNames = message.getFrom()[0].toString();
				System.out.println("To: " + message.getAllRecipients()[0]);
				System.out.println("Date: " + message.getReceivedDate());
				System.out.println("Size: " + message.getSize());
				System.out.println("Flags: " + message.getFlags());
				System.out.println("ContentType: " + message.getContentType());
				if (fromNames.contains(fromName)) {
					flag = true;
					message.setFlag(Flag.DELETED, true);

					if (message.getContentType().contains("multipart")) {
						MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
						result = getTextFromMimeMultipart(mimeMultipart);
					}

					if (message.isMimeType("text/plain") || message.isMimeType("text/html")) {
						result = message.getContent().toString();
					}
				}
				if (flag == true) {
					inbox.close(true);
					break;
				}
				if (count == 10)
					break;
			}
		}
		return result;
	}

	public String getAliasName(String userName, String password, String fromName) throws Exception {
		boolean flag = false;
		int count = 0;
		store = authenticate(userName, password);
		inbox = store.getFolder("INBOX");
		inbox.open(Folder.READ_WRITE);
		Message messages[] = inbox.search(new FlagTerm(new Flags(Flag.SEEN), false));
		if (messages.length == 0) {
			Thread.sleep(10000);
		} else {
			for (int i = messages.length - 1; i >= 0; i--) {
				Message message = messages[i];
				count++;
				System.out.println("Subject: " + message.getSubject());
				System.out.println("From: " + message.getFrom()[0]);
				String fromNames = message.getFrom()[0].toString();
				System.out.println("To: " + message.getAllRecipients()[0]);
				System.out.println("Date: " + message.getReceivedDate());
				System.out.println("Size: " + message.getSize());
				System.out.println("Flags: " + message.getFlags());
				System.out.println("ContentType: " + message.getContentType());
				if (fromNames.contains(fromName)) {
					flag = true;
					message.setFlag(Flag.DELETED, true);
					return fromNames;
				}
				if (flag == true) {
					inbox.close(true);
					break;
				}
				if (count == 10)
					break;
			}
		}
		return null;
	}

	public String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws MessagingException, IOException {
		String result = "";
		int count = mimeMultipart.getCount();
		for (int i = 0; i < count; i++) {
			BodyPart bodyPart = mimeMultipart.getBodyPart(i);
			if (bodyPart.isMimeType("text/plain")) {
				result = result + "\n" + bodyPart.getContent();
				break;
			} else if (bodyPart.isMimeType("text/html")) {
				result = result + "\n" + bodyPart.getContent();
				break;
			} else if (bodyPart.getContent() instanceof MimeMultipart) {
				result = result + getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent());
			}
		}
		return result;
	}

	// App Specific Method
	public void sendEmail(String senderEmail, String senderPassword, String toEmails, String ccEmails, String bccEmails,
			String subject, String body, String testRunStatus) throws MessagingException {
		final String user = senderEmail;
		final String password = senderPassword;

		InternetAddress[] to = InternetAddress.parse(toEmails);
		InternetAddress[] cc = InternetAddress.parse(ccEmails);
		InternetAddress[] bcc = InternetAddress.parse(bccEmails);

		// 1) get the session object
		Properties props = System.getProperties();
		String host = "smtp.gmail.com";
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");

		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user, password);
			}
		});

		// 2) compose message
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(user));
			message.addRecipients(Message.RecipientType.TO, to);
			message.addRecipients(Message.RecipientType.CC, cc);
			message.addRecipients(Message.RecipientType.BCC, bcc);

			if (testRunStatus.equalsIgnoreCase("fail")) {
				message.setSubject("TEST EXECUTION FAILED - " + subject);
			} else {
				message.setSubject("TEST EXECUTION PASSED - " + subject);
			}

			// 3) create MimeBodyPart object and set your message text
			BodyPart messageBodyPart1 = new MimeBodyPart();
			messageBodyPart1.setText(body);

			// 4) create new MimeBodyPart object and set DataHandler object to this object
			MimeBodyPart messageBodyPart2 = new MimeBodyPart();

			String filename = basePath + File.separator + "Extent Reports/Zipped Report.zip";
			DataSource source = new FileDataSource(filename);
			messageBodyPart2.setDataHandler(new DataHandler(source));
			if (testRunStatus.equalsIgnoreCase("fail")) {
				messageBodyPart2.setFileName("TEST EXECUTION FAILED - Test Automation Report.zip");
			} else {
				messageBodyPart2.setFileName("TEST EXECUTION PASSED - Test Automation Report.zip");
			}
			// messageBodyPart2.setFileName("Test Automation Report.zip");

			// 5) create Multipart object and add MimeBodyPart objects to this object
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart1);
			multipart.addBodyPart(messageBodyPart2);

			// 6) set the multiplart object to the message object
			message.setContent(multipart);

			// 7) send message
			Transport.send(message);

			// System.out.println("message sent....");
		} catch (MessagingException ex) {
			ex.printStackTrace();
		}
	}

	public String getToMailPerson(String userName, String password, String subject) throws Exception {
		boolean flag = false;
		int count = 0;
		store = authenticate(userName, password);
		inbox = store.getFolder("INBOX");
		inbox.open(Folder.READ_WRITE);
		Message messages[] = inbox.search(new FlagTerm(new Flags(Flag.SEEN), false));
		if (messages.length == 0) {
			Thread.sleep(10000);
		} else {
			for (int i = messages.length - 1; i >= 0; i--) {
				Message message = messages[i];
				count++;
				System.out.println("Subject: " + message.getSubject());
				System.out.println("From: " + message.getFrom()[0]);
				System.out.println("To: " + message.getAllRecipients()[0]);
				System.out.println("Date: " + message.getReceivedDate());
				System.out.println("Size: " + message.getSize());
				System.out.println("Flags: " + message.getFlags());
				System.out.println("ContentType: " + message.getContentType());
				if (message.getSubject().contains(subject)) {
					flag = true;
					message.setFlag(Flag.DELETED, true);
					if (message.getContentType().contains("multipart")) {
						result = message.getAllRecipients()[0].toString();
					}
					if (message.isMimeType("text/plain") || message.isMimeType("text/html")) {
						result = message.getAllRecipients()[0].toString();
					}
				}
				if (flag == true) {
					inbox.close(true);
					break;
				}
				if (count == 10)
					break;
			}
		}
		return result;
	}

	public String getSubjectOfReceivedMail(String userName, String password, String subject) throws Exception {
		String requiredSubject = null;
		int count = 0;
		store = authenticate(userName, password);
		inbox = store.getFolder("INBOX");
		inbox.open(Folder.READ_WRITE);
		Message messages[] = inbox.search(new FlagTerm(new Flags(Flag.SEEN), false));
		if (messages.length == 0) {
			Thread.sleep(10000);
		} else {
			for (int i = messages.length - 1; i >= 0; i--) {
				Message message = messages[i];
				count++;
				// System.out.println("Subject: " + message.getSubject());
				// System.out.println("From: " + message.getFrom()[0]);
				// System.out.println("To: " + message.getAllRecipients()[0]);
				// System.out.println("Date: " + message.getReceivedDate());
				// System.out.println("Size: " + message.getSize());
				// System.out.println("Flags: " + message.getFlags());
				// System.out.println("ContentType: " + message.getContentType());
				if (message.getSubject().contains(subject)) {
					requiredSubject = message.getSubject();
					message.setFlag(Flag.DELETED, true);
					inbox.close(true);
					break;
				}
				if (count == 10)
					break;
			}
		}
		return requiredSubject;
	}

	public String getSubjectOfReceivedMailFromFirstMail(String userName, String password, String subject)
			throws Exception { // by selenium+6
		String requiredSubject = null;
		int count = 0;
		store = authenticate(userName, password);
		inbox = store.getFolder("INBOX");
		inbox.open(Folder.READ_WRITE);
		Message messages[] = inbox.search(new FlagTerm(new Flags(Flag.SEEN), false));
		if (messages.length == 0) {
			Thread.sleep(10000);
		} else {
			for (int i = messages.length - 1; i >= 0; i--) {
				Message message = messages[i];
				count++;
				System.out.println("Subject: " + message.getSubject());
				System.out.println("From: " + message.getFrom()[0]);
				System.out.println("To: " + message.getAllRecipients()[0]);
				System.out.println("Date: " + message.getReceivedDate());
				System.out.println("Size: " + message.getSize());
				System.out.println("Flags: " + message.getFlags());
				System.out.println("ContentType: " + message.getContentType());
				if (message.getSubject().contains(subject)) {
					requiredSubject = message.getSubject();
					message.setFlag(Flag.DELETED, true);
					inbox.close(true);
					break;
				}
				if (count == 1)
					break;
			}
		}
		return requiredSubject;
	}

	public Boolean getCountOfTwoMailWithSameSubject(String userName, String password, String subject, int noOfMails)
			throws Exception {
		store = authenticate(userName, password);
		inbox = store.getFolder("INBOX");
		inbox.open(Folder.READ_WRITE);
		Message messages[] = inbox.search(new FlagTerm(new Flags(Flag.SEEN), false));
		if (messages.length != 0) {
			int noOfSubjects = 0;
			int count = 0;
			for (int i = messages.length - 1; i >= 0; i--) {
				count++;
				Message message = messages[i];
				if (message.getSubject().contains(subject)) {
					message.setFlag(Flag.DELETED, true);
					noOfSubjects++;
				}
				if (count == noOfMails)
					break;
			}
			if (noOfSubjects == 2) {

				return true;
			}
		}
		inbox.close(true);
		return false;
	}

	public void sendMailUsingSES(String smtpServerHost, String smtpServerPort, String smtpUserName,
			String smtpUserPassword, String fromUserEmail, String fromUserFullName, String toEmail, String subject,
			String body) {
		try {
			Properties props = System.getProperties();
			props.put("mail.transport.protocol", "smtps");
			props.put("mail.smtp.port", smtpServerPort);
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.required", "true");
			props.put("mail.smtp.ssl.enable", "true");

			Session session = Session.getDefaultInstance(props);

			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(fromUserEmail, fromUserFullName));
			msg.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
			msg.setSubject(subject);

			BodyPart messageBodyPart1 = new MimeBodyPart();
			messageBodyPart1.setText(body);

			MimeBodyPart messageBodyPart2 = new MimeBodyPart();

			String filePath = basePath + File.separator + "/Extent Reports";
			String zipPath = basePath + File.separator + "/extentreport.zip";

			Path zipFileCheck = Paths.get(zipPath);
	        if(Files.exists(zipFileCheck)) { // Attention here it is deleting the old file, if it already exists
	            Files.delete(zipFileCheck);
	            System.out.println("Deleted");
	        }
	        Path zipFile = Files.createFile(Paths.get(zipPath));

	        Path sourceDirPath = Paths.get(filePath);
	        try (ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(zipFile));
	             Stream<Path> paths = Files.walk(sourceDirPath)) {
	            paths
	                    .filter(path -> !Files.isDirectory(path))
	                    .forEach(path -> {
	                        ZipEntry zipEntry = new ZipEntry(sourceDirPath.relativize(path).toString());
	                        try {
	                            zipOutputStream.putNextEntry(zipEntry);
	                            
	                            Files.copy(path, zipOutputStream);
	                            zipOutputStream.closeEntry();
	                        } catch (IOException e) {
	                            System.err.println(e);
	                        }
	                    });
	        }

			DataSource source = new FileDataSource(zipPath);
			messageBodyPart2.setDataHandler(new DataHandler(source));
			messageBodyPart2.setFileName("Extent Report.zip");

			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart1);
			multipart.addBodyPart(messageBodyPart2);

			msg.setContent(multipart);

			Transport transport = session.getTransport();
			transport.connect(smtpServerHost, smtpUserName, smtpUserPassword);
			transport.sendMessage(msg, msg.getAllRecipients());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * To send extent report via email
	 */
	public void sendEmail(String senderEmail, String senderPassword, String toEmails, String subject, String body)
			throws Exception {
		final String user = senderEmail;
		final String password = senderPassword;
		InternetAddress[] to = InternetAddress.parse(toEmails);

		// 1) get the session object
		Properties props = System.getProperties();
		String host = "smtp.gmail.com";
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.ssl.trust", host);
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");
		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user, password);
			}
		});

		// 2) compose message
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(user));
			message.addRecipients(Message.RecipientType.TO, to);
			message.setSubject(subject);

			// 3) create MimeBodyPart object and set your message text
			BodyPart messageBodyPart1 = new MimeBodyPart();
			messageBodyPart1.setText(body);

			// 4) create new MimeBodyPart object and set DataHandler object to this object
			MimeBodyPart messageBodyPart2 = new MimeBodyPart();

			String filePath = basePath + File.separator + "/Extent Reports/extentreport.html";
			String zipPath = basePath + File.separator + "/Extent Reports/extentreport.zip";

			try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipPath))) {
				File fileToZip = new File(filePath);
				zipOut.putNextEntry(new ZipEntry(fileToZip.getName()));
				Files.copy(fileToZip.toPath(), zipOut);
			}

			String filename = basePath + File.separator + "/Extent Reports/extentreport.zip";

			DataSource source = new FileDataSource(filename);
			messageBodyPart2.setDataHandler(new DataHandler(source));
			messageBodyPart2.setFileName("Extent Report.zip");

			// 5) create Multipart object and add MimeBodyPart objects to this
			// object
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart1);
			multipart.addBodyPart(messageBodyPart2);

			// 6) set the multiplart object to the message object
			message.setContent(multipart);

			// 7) send message
			Transport.send(message);

			Reporter.log("Extent Report email sent");

		} catch (MessagingException ex) {
			ex.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}