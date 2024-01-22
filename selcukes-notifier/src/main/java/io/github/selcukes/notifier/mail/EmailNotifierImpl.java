/*
 *  Copyright (c) Ramesh Babu Prudhvi.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.github.selcukes.notifier.mail;

import io.github.selcukes.commons.config.Config;
import io.github.selcukes.commons.config.ConfigFactory;
import io.github.selcukes.commons.exception.ConfigurationException;
import jakarta.activation.DataHandler;
import jakarta.activation.FileDataSource;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import lombok.CustomLog;
import lombok.SneakyThrows;

import java.io.File;
import java.util.Arrays;
import java.util.Properties;
import java.util.function.Consumer;

/**
 * Manages the sending of emails using JavaMail API.
 */
@CustomLog
class EmailNotifierImpl implements EmailNotifier {

    private final Config.MailConfig mailConfig;

    public EmailNotifierImpl() {
        this.mailConfig = ConfigFactory.getConfig().getNotifier().getMail();
    }

    /**
     * Sends an email with the specified subject, body, and optional
     * attachments.
     *
     * @param subject     The subject of the email.
     * @param body        The body content of the email.
     * @param attachments Array of file paths to be attached to the email.
     */
    @SneakyThrows
    public void sendMail(String subject, String body, String... attachments) {
        Properties properties = createMailProperties();
        Session session = createMailSession(properties);

        MimeMessage message = new MimeMessage(session);
        setMailMessageDetails(message, subject, body);

        Arrays.stream(attachments)
                .map(this::createAttachmentBodyPart)
                .forEach(addBodyPartToMessage(message));

        Transport.send(message);
        logger.info(() -> "Email sent successfully!");
    }

    private Properties createMailProperties() {
        var properties = new Properties();
        properties.put("mail.smtp.host", mailConfig.getHost());
        properties.put("mail.smtp.port", mailConfig.getPort());
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.ssl.enable", "true");
        return properties;
    }

    private Session createMailSession(Properties properties) {
        return Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mailConfig.getUsername(), mailConfig.getPassword());
            }
        });
    }

    @SneakyThrows
    private void setMailMessageDetails(MimeMessage message, String subject, String body) {
        message.setFrom(new InternetAddress(mailConfig.getFrom()));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailConfig.getTo()));
        message.setSubject(subject);

        var textPart = new MimeBodyPart();
        textPart.setText(body);

        var multipart = new MimeMultipart();
        multipart.addBodyPart(textPart);
        message.setContent(multipart);
    }

    @SneakyThrows
    private MimeBodyPart createAttachmentBodyPart(String attachmentPath) {
        var attachmentPart = new MimeBodyPart();
        var source = new FileDataSource(new File(attachmentPath));
        attachmentPart.setDataHandler(new DataHandler(source));
        attachmentPart.setFileName(new File(attachmentPath).getName());
        return attachmentPart;
    }

    @SneakyThrows
    private Consumer<MimeBodyPart> addBodyPartToMessage(MimeMessage message) {
        var multipart = (MimeMultipart) message.getContent();
        return mimeBodyPart -> {
            try {
                multipart.addBodyPart(mimeBodyPart);
            } catch (MessagingException e) {
                throw new ConfigurationException("Error adding body part to message", e);
            }
        };
    }

}
