/**
 * 
 */

package com.nagoya.middleware.service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nagoya.dao.util.StringUtil;
import com.nagoya.middleware.main.ServerPropertiesProvider;
import com.nagoya.middleware.main.ServerProperty;
import com.nagoya.middleware.util.FileReader;
import com.nagoya.model.dbo.contract.ContractDBO;

/**
 * @author flba
 *
 */
public class MailService {

    private static final String DEFAULT_LANGUAGE = "en";
    private static final String DEFAULT_DIR      = "mail/";

    private static final Logger LOGGER           = LogManager.getLogger(MailService.class);

    private String              language;

    public MailService(String language) {
        this.language = DEFAULT_LANGUAGE;
        setLanguage(language);
    }

    private void setLanguage(String language) {
        if (StringUtil.isNullOrBlank(language)) {
            return;
        }
        // if we have just one character - no match possible
        if (language.length() < 2) {
            return;
        }
        // if we have more than one character - strip it to two
        if (language.length() > 2) {
            language = language.substring(1, 3);
        }
        // verify if it exists, if so set the new language
        boolean existentLanguage = FileReader.directoryExists(DEFAULT_DIR + language);
        if (existentLanguage) {
            this.language = language;
        }
    }

    private boolean isTestMode() {
        String property = System.getProperty("nagoya.test");
        if (StringUtil.isNotNullOrBlank(property)) {
            if (property.equals("true")) {
                return true;
            }
        }
        return false;
    }

    public void sendMail(String destinationAddress, String subject, String content) {
        if (isTestMode()) {
            return;
        }

        final String username = EMailPropertiesProvider.getString(EMailMessageProperty.USERNAME);
        final String password = EMailPropertiesProvider.getString(EMailMessageProperty.PASSWORD);

        Properties props = new Properties();

        String host = EMailPropertiesProvider.getString(EMailMessageProperty.MAIL_SMTP_HOST);

        String auth = EMailPropertiesProvider.getString(EMailMessageProperty.MAIL_SMTP_AUTH);
        if (StringUtil.isNotNullOrBlank(auth)) {
            props.put("mail.smtp.ssl.trust", host);
            props.put(EMailMessageProperty.MAIL_SMTP_AUTH.getProperty(), auth);
        }

        String starttls = EMailPropertiesProvider.getString(EMailMessageProperty.MAIL_SMTP_STARTTLS_ENABLE);
        if (StringUtil.isNotNullOrBlank(starttls)) {
            props.put(EMailMessageProperty.MAIL_SMTP_STARTTLS_ENABLE.getProperty(), starttls);
        }

        props.put(EMailMessageProperty.MAIL_SMTP_HOST.getProperty(), host);
        props.put(EMailMessageProperty.MAIL_SMTP_PORT.getProperty(), EMailPropertiesProvider.getString(EMailMessageProperty.MAIL_SMTP_PORT));

        Session session = null;
        if (StringUtil.isNotNullOrBlank(username) && StringUtil.isNotNullOrBlank(password)) {
            session = Session.getInstance(props, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });
        } else {
            session = Session.getDefaultInstance(props);
        }

        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(EMailPropertiesProvider.getString(EMailMessageProperty.EMAIL)));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinationAddress));
            message.setSubject(subject);

            Multipart multiPart = new MimeMultipart();
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(content, "text/html; charset=UTF-8");
            multiPart.addBodyPart(htmlPart);
            message.setContent(multiPart);

            Transport.send(message);

        } catch (AddressException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (MessagingException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

        LOGGER.debug("E-Mail sent successfully to: " + destinationAddress);
    }

    public void sendRegistratationMail(String destinationAddress, String registrationToken, Date deadline) {
        String htmlPath = DEFAULT_DIR + this.language + "/" + "mail_registration.html";
        String registrationMailText = FileReader.readFile(htmlPath, StandardCharsets.UTF_8);

        String link = ServerPropertiesProvider.getString(ServerProperty.SERVER_HOST_NAME);
        link += ServerPropertiesProvider.getString(ServerProperty.SERVER_MAIL_CONFIRMATION_PATH);
        link += registrationToken;

        registrationMailText = registrationMailText.replace("###link###", link);
        registrationMailText = registrationMailText.replace("###deadline###", deadline.toString());

        String subject = getTitle(registrationMailText);
        sendMail(destinationAddress, subject, registrationMailText);
    }

    public void sendPasswordResetMail(String destinationAddress, String registrationToken, Date deadline) {
        String htmlPath = DEFAULT_DIR + this.language + "/" + "mail_pwreset.html";
        String registrationMailText = FileReader.readFile(htmlPath, StandardCharsets.UTF_8);

        String link = ServerPropertiesProvider.getString(ServerProperty.SERVER_HOST_NAME);
        link += ServerPropertiesProvider.getString(ServerProperty.SERVER_MAIL_CONFIRMATION_PATH);
        link += registrationToken;

        registrationMailText = registrationMailText.replace("###link###", link);
        registrationMailText = registrationMailText.replace("###deadline###", deadline.toString());

        String subject = getTitle(registrationMailText);
        sendMail(destinationAddress, subject, registrationMailText);
    }

    public void sendDeleteAccountMail(String destinationAddress, String registrationToken, Date deadline) {
        String htmlPath = DEFAULT_DIR + this.language + "/" + "mail_deleteaccount.html";
        String registrationMailText = FileReader.readFile(htmlPath, StandardCharsets.UTF_8);

        String link = ServerPropertiesProvider.getString(ServerProperty.SERVER_HOST_NAME);
        link += ServerPropertiesProvider.getString(ServerProperty.SERVER_MAIL_CONFIRMATION_PATH);
        link += registrationToken;

        registrationMailText = registrationMailText.replace("###link###", link);
        registrationMailText = registrationMailText.replace("###deadline###", deadline.toString());

        String subject = getTitle(registrationMailText);
        sendMail(destinationAddress, subject, registrationMailText);
    }

    private String getTitle(String content) {
        if (StringUtil.isNullOrBlank(content)) {
            return null;
        }
        int i1 = content.indexOf("<title>");
        int i2 = content.indexOf("</title>");
        if (i1 == -1 || i2 == -1) {
            return null;
        }

        return content.substring(i1 + 7, i2);
    }

    public void sendContractCreationConfirmation(String emailAddress) {
        String htmlPath = DEFAULT_DIR + this.language + "/" + "mail_genetic_resource_transfer_creation_confirmation.html";
        String registrationMailText = FileReader.readFile(htmlPath, StandardCharsets.UTF_8);

        String subject = getTitle(registrationMailText);
        sendMail(emailAddress, subject, registrationMailText);

    }

    public void sendContractAcceptancePending(String token, Date deadline, String emailAddress) {
        String htmlPath = DEFAULT_DIR + this.language + "/" + "mail_genetic_resource_transfer_pending_acceptance.html";
        String registrationMailText = FileReader.readFile(htmlPath, StandardCharsets.UTF_8);

        String link = ServerPropertiesProvider.getString(ServerProperty.SERVER_HOST_NAME);
        link += ServerPropertiesProvider.getString(ServerProperty.SERVER_MAIL_CONFIRMATION_PATH);
        link += token;

        registrationMailText = registrationMailText.replace("###link###", link);
        registrationMailText = registrationMailText.replace("###deadline###", deadline.toString());

        String subject = getTitle(registrationMailText);
        sendMail(emailAddress, subject, registrationMailText);

    }

    public void sendContractAccepted(ContractDBO contract) {
        // TODO Auto-generated method stub

    }
}
