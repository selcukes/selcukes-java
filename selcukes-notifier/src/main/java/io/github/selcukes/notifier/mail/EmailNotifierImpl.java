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
import io.github.selcukes.commons.exception.NotifierException;
import jakarta.activation.DataHandler;
import jakarta.activation.FileDataSource;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import lombok.CustomLog;
import lombok.SneakyThrows;

import java.io.File;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.stream.Stream;

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

    public void sendMail(String subject, String body, String... attachments) {
        var properties = new Properties();
        properties.putAll(defaultEmailConfig());
        try {
            var session = createMailSession(properties);

            var message = new MimeMessage(session);
            setMailMessageDetails(message, subject, body);

            Stream.of(attachments)
                    .map(this::createAttachmentBodyPart)
                    .forEach(addBodyPartToMessage(message));

            Transport.send(message);
            logger.info(() -> "Email sent successfully!");
        } catch (Exception e) {
            throw new NotifierException("Failed sending email. Please check the provided information and try again.",
                e);

        }
    }

    private Map<String, Object> defaultEmailConfig() {
        Objects.requireNonNull(mailConfig,
            "Mail configuration is missing. Please ensure that 'mail' configuration is specified under 'notifier' in the 'selcukes.yaml' file.");

        String host = Objects.requireNonNull(mailConfig.getHost(),
            "Mail host is not configured. Please specify the 'host' property in the Mail configuration.");

        return Map.of(
            "mail.smtp.host", host,
            "mail.smtp.port", mailConfig.getPort(),
            "mail.smtp.auth", "true",
            "mail.smtp.ssl.enable", "true");
    }

    private Session createMailSession(Properties properties) {
        return Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mailConfig.getUsername(), mailConfig.getPassword());
            }
        });
    }

    private void setMailMessageDetails(MimeMessage message, String subject, String body) {
        try {
            message.setFrom(new InternetAddress(mailConfig.getFrom()));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailConfig.getTo()));
            message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(mailConfig.getCc()));
            message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(mailConfig.getBcc()));
            message.setSubject(subject);

            var textPart = new MimeBodyPart();
            textPart.setText(body);

            var multipart = new MimeMultipart();
            multipart.addBodyPart(textPart);
            message.setContent(multipart);
        } catch (Exception e) {
            throw new NotifierException("Failed sending email.", e);
        }
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
                throw new NotifierException("Error adding body part to message", e);
            }
        };
    }
}
