package com.warys.scrooge.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Value("${mail.auth.host}")
    private String host;
    @Value("${mail.auth.port}")
    private int port;
    @Value("${mail.auth.username}")
    private String username;
    @Value("${mail.auth.password}")
    private String password;
    @Value("${mail.auth.protocol}")
    private String protocol;
    @Value("${mail.properties.mail.smtp.auth.enable}")
    private boolean withAuth;
    @Value("${mail.properties.mail.smtp.starttls.enable}")
    private boolean starttlsEnable;
    @Value("${mail.properties.debug.enable}")
    private boolean debug;

    @Bean
    public JavaMailSender getJavaMailSender() {
        var mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);

        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", protocol);
        props.put("mail.smtp.auth", withAuth);
        props.put("mail.smtp.starttls.enable", starttlsEnable);
        props.put("mail.debug", debug);

        return mailSender;
    }
}
