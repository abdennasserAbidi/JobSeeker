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

    public void sendResetToken1(String email, String token) {
        OkHttpClient client = new OkHttpClient();
        String url = "myapp://reset-password/" + token;

        String json = """
                {
                  "from": "onboarding@resend.dev",
                  "to": "%s",
                  "subject": "%s",
                  "html": "%s"
                }
                """.formatted(email, "Password Reset Request", url);

        Request request = new Request.Builder()
                .url("https://api.resend.com/emails")
                .post(RequestBody.create(json, MediaType.parse("application/json")))
                .addHeader("Authorization", "Bearer " + "")
                .build();

        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendResetToken(String email, String token) {
        String url = "myapp://resetpassword/" + token;
        String link = "https://jobdeal?token=" + token;

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Password Reset Request");
        mailMessage.setText("To reset your password, click here : " + url);


        String html = """
                <html>
                  <body>
                    <h3>You have a new invitation</h3>
                    <p>Click the link below to open the app:</p>
                    <a href="%s" target="_blank">Open in app</a>
                  </body>
                </html>
                """.formatted(link);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject("Password Reset Request");
            helper.setText(html, true);
            //helper.setFrom("yourapp@gmail.com");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }


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
