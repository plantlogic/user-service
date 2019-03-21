package edu.csumb.spring19.capstone.services;

import edu.csumb.spring19.capstone.config.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import static j2html.TagCreator.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class MailService {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private AppConfig appConfig;

    @Value("${SMTP_FROM: }")
    private String fromAddress;

    private void sendMail(String to, String subject, String body) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper h = new MimeMessageHelper(message, false, "utf-8");
        message.setContent(body, "text/html");
        h.setTo(to);
        h.setFrom(fromAddress);
        h.setSubject(subject);
        mailSender.send(message);
    }

    public void newAccountCreated(String to, String name, String username, String token) throws MessagingException {
        sendMail(to, "New " + appConfig.getAppName() + " Account",
              html(
                    p("Hi " + name + ","),

                    p("An administrator has created you a " + appConfig.getAppName() + " account."),

                    p(
                          li("Your username is: " + username), br(),
                          li("Your temporary password is: " + token)
                    ),

                    (appConfig.hasAppURL() ?
                          p(text("You can login here: "),
                                a("https://" + appConfig.getAppURL()).withHref("https://" + appConfig.getAppURL()))
                          : p("Contact your administrator for a login link.")
                    ),

                    p(text("Best,"), br(),
                          text(appConfig.getAppName() + " Team"))
              ).render()
        );
    }

    public void passwordReset(String to, String name, String token) throws MessagingException {
        sendMail(to,appConfig.getAppName() + " Password Reset",
              html(
                    p("Hi " + name + ","),

                    p("You are receiving this email because your " + appConfig.getAppName() + " password has been reset."),

                    p(li("Your temporary password is: " + token)),

                    (appConfig.hasAppURL() ?
                          p(text("You can login here: "),
                                a("https://" + appConfig.getAppURL()).withHref("https://" + appConfig.getAppURL()))
                          : p("Contact your administrator for a login link.")
                    ),

                    p(text("Best,"), br(),
                          text(appConfig.getAppName() + " Team"))
              ).render()
        );
    }

    public static void main() {
        System.out.println();
    }
}
