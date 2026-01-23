package com.myjob.jobseeker.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    // Inject the environment variable value
    /*@Value("${resend.api.key}")
    private String resendApiKey;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.host}")
    private String host;*/

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendResetToken(String email, String token) {

        /*SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Password Reset Request");
        mailMessage.setText("To reset your password, click here : " + link);*/

        String html = """
                <html>
                  <body>
                    <h3>You have a new invitation</h3>
                    <p>Click the link below to open the app:</p>
                    <a href="%s" target="_blank">Open in app</a>
                  </body>
                </html>
                """.formatted("https://jobdeal?token=" + token);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject("Password Reset Request");
            helper.setText(html, true);
            //helper.setFrom("yourapp@gmail.com");
        } catch (MessagingException e) {
            System.out.println("errrorrrrrr email   :   "+e.getMessage() );
            throw new RuntimeException(e);
        }

        System.out.println("successs  email   :   "+message);
        mailSender.send(message);
    }

    public void sendTokenValidation(String email) {
        int token = ThreadLocalRandom.current().nextInt(1000, 10000);
        String url = "myApp://notification/" + token;
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Validate profile Request");
        mailMessage.setText("To validate your profile, click here : " + url);
        mailSender.send(mailMessage);
    }

    public void sendLinkValidation(String email) {
        int token = ThreadLocalRandom.current().nextInt(1000, 10000);
        String url = "myApp://notification/";
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Validate profile Request");
        mailMessage.setText("To validate your profile, click here : " + url);
        mailSender.send(mailMessage);
    }
}
