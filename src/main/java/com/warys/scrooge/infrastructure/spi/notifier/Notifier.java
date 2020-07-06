package com.warys.scrooge.infrastructure.spi.notifier;

public interface Notifier {

    void sendSimpleMessage(String to, String subject, String text);

    void sendSubscriptionMessage(String to);
}
