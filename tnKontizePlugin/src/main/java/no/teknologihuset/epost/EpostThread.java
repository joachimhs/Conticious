package no.teknologihuset.epost;

import no.haagensoftware.contentice.spi.StoragePlugin;
import no.haagensoftware.contentice.util.FileUtil;
import no.haagensoftware.contentice.util.IntegerParser;
import no.teknologihuset.handlers.BookingInquiryAssembler;
import no.teknologihuset.handlers.data.BookingInquiry;
import org.apache.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.net.PasswordAuthentication;
import java.util.Properties;

/**
 * Created by jhsmbp on 19/02/14.
 */
public class EpostThread implements Runnable {
    private static final Logger logger = Logger.getLogger(EpostThread.class.getName());

    private BookingInquiry bookingInquiry;
    private StoragePlugin storagePlugin;
    private String host;

    public EpostThread(String host, BookingInquiry bookingInquiry, StoragePlugin storagePlugin) {
        this.host = host;
        this.bookingInquiry = bookingInquiry;
        this.storagePlugin = storagePlugin;
    }

    @Override
    public void run() {
        if (bookingInquiry != null) {
            if (sendEmailWithSSL(bookingInquiry.getEpost(), bookingInquiry.getSubject(), bookingInquiry.getMessage())) {
                storagePlugin.setSubCategory(host, "emailsSent", bookingInquiry.getId(), BookingInquiryAssembler.convertBookingInquiryToSubCategory(bookingInquiry));
                storagePlugin.deleteSubcategory(host, "emailsNotSent", bookingInquiry.getId());
            }
        }
    }

    private boolean sendEmailWithSSL(String emailAddress, String emailSubject, String emailMessage) {
        boolean success = true;

        String host = System.getProperty("teknologihuset.smtp.server");
        int port = IntegerParser.parseIntegerFromString(System.getProperty("teknologihuset.smtp.port"), 587);
        final String username = System.getProperty("teknologihuset.smtp.username");;
        final String password = System.getProperty("teknologihuset.smtp.password");;

        Properties props = new Properties();
        props.put("mail.smtps.host", host);
        props.put("mail.smtps.auth", "true");
        props.put("mail.smtps.auth", "true");
        props.put("mail.smtps.starttls.enable", "true");
        props.put("mail.smtps.port", "" + port);
        props.put("mail.smtp.port", "" + port);

        //props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication(username, password);
            }
        });

        Transport transport = null;
        try {
            transport = session.getTransport("smtps");
            transport.connect(host, port, username, password);

            Message msg = buildMessage(session, emailAddress, emailSubject, emailMessage);
            transport.sendMessage(msg, msg.getAllRecipients());
            transport.close();
        } catch (MessagingException e) {
            e.printStackTrace();
            success = false;
        }

        return success;
    }

    private String stripNonEmailChars(String input) {
        String stripped = new String(input);

        if (stripped.startsWith("\"")) {
            stripped = stripped.substring(1);
        }
        if (stripped.endsWith("\"")) {
            stripped = stripped.substring(0, stripped.length()-1);
        }

        stripped = stripped.replace("\n", "");
        stripped = stripped.replace("\\n", "");
        stripped = stripped.replace("\\\"", "\"");

        return stripped;
    }

    private Message buildMessage(Session session, String emailAddress, String emailSubject, String emailMessage) throws AddressException, MessagingException {
        MimeMessage message = new MimeMessage(session);
        message.setHeader("Content-Type", "text/plain");
        message.setFrom(new InternetAddress("vert@teknologihuset.no"));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(stripNonEmailChars(emailAddress) + ", vert@teknologihuset.no, joachim@haagen-software.no"));
        message.setSubject(stripNonEmailChars(emailSubject));

        String htmlMessage = stripNonEmailChars(emailMessage);

        logger.info("HTML CONTENT: ");
        logger.info(htmlMessage);

        message.setContent(htmlMessage, "text/html; charset=utf-8");


        return message;
    }
}
