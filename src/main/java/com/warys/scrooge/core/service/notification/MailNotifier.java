package com.warys.scrooge.core.service.notification;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailNotifier implements Notifier {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(MailNotifier.class);
    private final JavaMailSender emailSender;
    @Value("${app.feature.emailer.enabled}")
    private boolean isEnables;

    @Autowired
    public MailNotifier(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendSimpleMessage(String to, String subject, String text) {
        logger.info("Sending message %s to % with subject %s");
        if (isEnables) {
            var message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            emailSender.send(message);
        }
    }

    public void sendSubscriptionMessage(String to) {
        sendSimpleMessage(to, "Subscription", "Thanks for you subscription in scrooge");
    }
}
