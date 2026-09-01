package com.thiwain.util;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class EmailSenderUtil {

    public String sendEmail(String mailSubject, String mailBody, String email) {

        String to = email;
        String from = "medagamathiwain@gmail.com";
        String subject = mailSubject;
        String htmlBody = mailBody;

        String host = "smtp.gmail.com";
        String port = "587";

        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // Replace with your Gmail credentials
        String username = "medagamathiwain@gmail.com";
        String password = "jafj cqvt gcwf btqi";

        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setContent(htmlBody, "text/html");

            Transport.send(message);
            return "OK";
        } catch (MessagingException mex) {
            mex.printStackTrace();
            return "Failed to send email: " + mex.getMessage();
        }
    }

}
