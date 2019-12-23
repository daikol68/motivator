package de.daikol.motivator.service;

import de.daikol.motivator.exception.MessageException;
import de.daikol.motivator.model.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by daikol on 12.10.2016.
 */
@Service
public class EmailService {

    /**
     * The from.
     */
    @Value("${smtp.sender:info@daikol.de}")
    private String sender;

    /**
     * The host to send emails.
     */
    @Value("${smtp.host:smtp.udag.de}")
    private String host;

    /**
     * The username for authentification.
     */
    @Value("${smtp.name:daikol-de-0003}")
    private String name;

    /**
     * The password for authentification.
     */
    @Value("${smtp.password}")
    private String password;

    public void sendRegistrationEmail(User user) {

        try {
            // Recipient's email ID needs to be mentioned.
            String to = user.getEmail();

            // create properties
            Properties properties = System.getProperties();

            // Setup mail server
            properties.setProperty("mail.smtp.host", host);
            properties.setProperty("mail.smtp.auth", "true");

            // set the authentification data
            Authenticator authenticator = new Authenticator();

            // Get the default Session object.
            Session session = Session.getInstance(properties, authenticator);

            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(sender));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject("Ihre Registrierung bei Challenger!");

            // Send the actual HTML message, as big as you like
            message.setContent(createRegistrationContent(user), "text/html");

            // Send message
            Transport.send(message);

        } catch (MessagingException e) {
            throw new MessageException(user.getEmail(), e);
        }
    }

    private String createRegistrationContent(User user) {
        StringBuilder builder = new StringBuilder();
        builder.append("<h1>").append("Hallo ").append(user.getName()).append(",").append("</h1>");
        builder.append("<h2>").append("sch&ouml;n, dass du die App Challenger installiert hast!").append("</h2>");
        builder.append("<h2>").append("Um deinen Account zu aktivieren klicke bitte ")
                .append("<a href=\"https://daikol.de/challenger-service/rest/registration/complete/")
                .append(user.getEmail()).append("/").append(user.getCode())
                .append("\">hier").append("</a>")
                .append("</h2>");

        return builder.toString();
    }

    private class Authenticator extends javax.mail.Authenticator {
        private PasswordAuthentication authentication;

        public Authenticator() {
            authentication = new PasswordAuthentication(name, password);
        }

        protected PasswordAuthentication getPasswordAuthentication() {
            return authentication;
        }
    }
}
