package com.warys.scrooge.core.service.notification;

public interface Notifier {

    void sendSimpleMessage(String to, String subject, String text);

    void sendSubscriptionMessage(String to);
}
