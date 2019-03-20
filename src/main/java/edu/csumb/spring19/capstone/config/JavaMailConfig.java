package edu.csumb.spring19.capstone.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class JavaMailConfig {
    @Value("${SMTP_HOST: }")
    private String host;
    @Value("${SMTP_PORT:587}")
    private Integer port;
    @Value("${SMTP_USERNAME: }")
    private String username;
    @Value("${SMTP_PASSWORD: }")
    private String password;
    @Value("${SMTP_TLS:true}")
    private String tlsEnabled;


    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);

        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", tlsEnabled);
        props.put("mail.debug", "false");

        return mailSender;
    }
}
