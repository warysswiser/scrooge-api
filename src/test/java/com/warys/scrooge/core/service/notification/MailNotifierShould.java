package com.warys.scrooge.core.service.notification;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class MailNotifierShould {

    private static final String TO = "mbugujude@gmail.com";
    private JavaMailSender emailSender = mock(JavaMailSender.class);
    private MailNotifier tested = new MailNotifier(emailSender);

    @Test
    public void send_subscription_message_when_notifier_is_enabled() {

        tested.setEnables(true);

        tested.sendSubscriptionMessage(TO);

        ArgumentCaptor<SimpleMailMessage> argumentCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(emailSender).send(argumentCaptor.capture());
        SimpleMailMessage capturedArgument = argumentCaptor.getValue();

        assertThat(new String[]{TO}).isEqualTo(capturedArgument.getTo());
    }

    @Test
    public void send_subscription_message_when_notifier_is_NOT_enabled() {
        tested.setEnables(false);

        tested.sendSubscriptionMessage(TO);

        verifyZeroInteractions(emailSender);
    }
}