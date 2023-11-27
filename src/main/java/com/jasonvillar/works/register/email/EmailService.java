package com.jasonvillar.works.register.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final Environment environment;

    public boolean sendSimpleMessage(String to, String subject, String text) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        try {
            String systemEmailAddress = this.environment.getProperty("spring.mail.username");

            if (systemEmailAddress != null) {
                helper.setFrom(systemEmailAddress);
                helper.setTo(to);
                helper.setSubject(subject);
                helper.setText(text, true);

                mailSender.send(mimeMessage);

                return true;
            } else {
                logger.trace("Not found the spring.mail.username property");
                return false;
            }
        } catch (MessagingException e) {
            logger.trace("Email could not be send", e);
            return false;
        }
    }
}
