package edu.csumb.spring19.capstone.services;

import edu.csumb.spring19.capstone.config.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    @Autowired
    private MailSender mailSender;

    @Autowired
    private AppConfig appConfig;

    @Value("${SMTP_FROM: }")
    private String fromAddress;

    private void sendMail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setFrom(fromAddress);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    public void newAccountCreated(String to, String name, String username, String token) {
        sendMail(to, "New " + appConfig.getAppName() + " Account",

              "Hi " + name + ",\n\n" +


                    "An administrator has created you a " + appConfig.getAppName() + " account.\n\n" +


                    "Your username is: " + username + "\n" +
                    "Your temporary password is: " + token + "\n\n" +


                    "Best,\n" +
                    appConfig.getAppName() + " Team"

        );
    }

    public void passwordReset(String to, String name, String token) {
        sendMail(to,appConfig.getAppName() + " Password Reset",

              "Hi " + name + ",\n\n" +


                    "You are receiving this email because your " + appConfig.getAppName() + " password has been reset.\n\n" +

                    "Your temporary password is: " + token + "\n\n" +


                    "Best,\n" +
                    appConfig.getAppName() + " Team"

        );
    }
}
