package com.myjob.jobseeker.services;

import java.util.concurrent.ThreadLocalRandom;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendResetToken(String email, String token) {
        String url = "http://192.168.110.209/" + token;
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Password Reset Request");
        mailMessage.setText("To reset your password, click here : " + url );
        mailSender.send(mailMessage);
    }

    public void sendTokenValidation(String email) {
        int token = ThreadLocalRandom.current().nextInt(1000, 10000);
        String url = "http://192.168.110.209/" + token;
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Validate profile Request");
        mailMessage.setText("To validate your profile, click here : " + url );
        mailSender.send(mailMessage);
    }

    public void sendLinkValidation(String email) {
        System.out.println("jdzkgkrhrgrgrzgzrgzr   sendLinkValidation  :  "+email);

        int token = ThreadLocalRandom.current().nextInt(1000, 10000);
        String url = "http://192.168.110.209/";
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Validate profile Request");
        mailMessage.setText("To validate your profile, click here : " + url );
        mailSender.send(mailMessage);
    }
}
