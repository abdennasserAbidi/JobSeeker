package com.myjob.jobseeker.services;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    // Inject the environment variable value
    @Value("${resend.api.key}")
    private String resendApiKey;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.host}")
    private String host;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendResetTokenResend(String email, String token) {
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
                .addHeader("Authorization", "Bearer " + resendApiKey)
                .build();

        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendResetToken(String email, String token) {
        String url = "myapp://resetpassword/" + token;
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(email);
        mailMessage.setTo(email);
        mailMessage.setSubject("Password Reset Request");
        mailMessage.setText("To reset your password, click here : " + url );
        System.out.println("qqqqqqqqqqqqqqqqqqqq   mailMessage   "+mailMessage);

        JavaMailSenderImpl javaMailSender = (JavaMailSenderImpl) mailSender;

        //JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(host);
        javaMailSender.setPort(port);
        javaMailSender.setUsername(username);
        javaMailSender.setPassword(password);
        javaMailSender.setProtocol("smtps");

        Properties props = javaMailSender.getJavaMailProperties();

        // For Port 465 (SSL)
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");

        // Timeouts
        props.put("mail.smtp.connectiontimeout", "10000");
        props.put("mail.smtp.timeout", "10000");
        props.put("mail.smtp.writetimeout", "10000");

        try {
            javaMailSender.testConnection();
            System.out.println("connection    SUCCESS");
        } catch (Exception e) {
            System.out.println("connection    FAILED  "+e.getMessage());
        }

        javaMailSender.send(mailMessage);
    }

    public void sendTokenValidation(String email) {
        int token = ThreadLocalRandom.current().nextInt(1000, 10000);
        String url = "myApp://notification/" + token;
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Validate profile Request");
        mailMessage.setText("To validate your profile, click here : " + url );
        mailSender.send(mailMessage);
    }

    public void sendLinkValidation(String email) {
        int token = ThreadLocalRandom.current().nextInt(1000, 10000);
        String url = "myApp://notification/";
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Validate profile Request");
        mailMessage.setText("To validate your profile, click here : " + url );
        mailSender.send(mailMessage);
    }
}
