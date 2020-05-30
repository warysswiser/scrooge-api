package com.warys.scrooge.infrastructure.adapter.notifier;

public interface Notifier {

    void sendSimpleMessage(String to, String subject, String text);

    void sendSubscriptionMessage(String to);
}
