package com.warys.scrooge.infrastructure.spi.notifier;

import com.google.common.annotations.VisibleForTesting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MailNotifier implements Notifier {

    private static final String SUBSCRIPTION_MESSAGE = "Thanks for you subscription in scrooge";
    private final JavaMailSender emailSender;
    @Value("${feature.emailer.enabled}")
    private boolean isEnables;

    public MailNotifier(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendSimpleMessage(String to, String subject, String text) {
        log.info("Sending message {} to {} with subject {}", text, to, subject);
        if (isEnables) {
            var message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            emailSender.send(message);
        }
    }

    public void sendSubscriptionMessage(String to) {
        sendSimpleMessage(to, "Subscription", SUBSCRIPTION_MESSAGE);
    }

    @VisibleForTesting
    public void setEnables(boolean enables) {
        isEnables = enables;
    }
}
