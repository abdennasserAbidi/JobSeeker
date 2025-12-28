package com.myjob.jobseeker.services;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    // Inject the environment variable value
    @Value("${resend.api.key}")
    private String resendApiKey;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendResetToken(String email, String token) {
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

    public void sendResetToken1(String email, String token) {
        String url = "https://192.168.1.13/reset-password/" + token;
        //String url = "http://192.168.1.13/" + token;
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Password Reset Request");
        mailMessage.setText("To reset your password, click here : " + url );
        System.out.println("qqqqqqqqqqqqqqqqqqqq   mailMessage   "+mailMessage);

        mailSender.send(mailMessage);
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
