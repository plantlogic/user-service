package edu.csumb.spring19.capstone.services;

import edu.csumb.spring19.capstone.config.AppConfig;
import edu.csumb.spring19.capstone.config.JavaMailConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    @Autowired
    private MailSender mailSender;

    @Autowired
    private AppConfig appConfig;

    private void sendMail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setFrom(JavaMailConfig.getFromAddress());
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    public void newAccountCreated(String to, String name, String username, String token) {
        sendMail(to, "New " + appConfig.getAppName() + " Account",
              "Hi " + name + ",\n\n\n" +
                    "The owner of your system has created you a new " + appConfig.getAppName() + " account.\n\n" +
                    "Your username is: " + username + "\n" +
                    "Your temporary password is: " + token + "\n\n" +
                    "Best,\n" +
                    appConfig.getAppName()
        );
    }
}
