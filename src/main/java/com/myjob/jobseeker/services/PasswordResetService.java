package com.myjob.jobseeker.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.myjob.jobseeker.model.PasswordResetToken;
import com.myjob.jobseeker.model.User;
import com.myjob.jobseeker.repo.PasswordResetTokenRepository;
import com.myjob.jobseeker.repo.UserRepository;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService {

    private final PasswordResetTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public PasswordResetService(PasswordResetTokenRepository tokenRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String createPasswordResetTokenForUser(String userEmail) {
        System.err.println("haw l email : "+userEmail);
        Optional<User> user = userRepository.findByEmail(userEmail);
        System.out.println("ktykjhkljkkkkkkkk  "+user);

        if (user.isEmpty()) {
            throw new IllegalArgumentException("User not found with email: " + userEmail);
        }

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setId(user.get().getId()+10);
        resetToken.setToken(token);
        resetToken.setUserId(user.get().getId());
        resetToken.setExpiryDate(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)); // 1 hour expiry

        tokenRepository.save(resetToken);
        
        return token;
    }

    public boolean validatePasswordResetToken(String token) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token);
        System.err.println("tokenalaaaaaa : "+resetToken.getExpiryDate());
        System.err.println("tokenalaaaaaa1 : "+new Date());
        System.err.println("tokenalaaaaaa2 : "+resetToken.getExpiryDate().before(new Date()));
        if (resetToken == null || resetToken.getExpiryDate().before(new Date())) {
            return false;
        }
        return true;
    }

    public void updatePassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token);
        System.err.println("token : "+resetToken);
        User user = userRepository.findById(resetToken.getUserId()).orElseThrow(() -> new IllegalArgumentException("Invalid token"));

        user.setEmail(user.getEmail());
        user.setFullName(user.getFullName());
        user.setId(resetToken.getUserId());

        user.setPassword(passwordEncoder.encode(newPassword)); // You should hash the password here
        user.setIsResetPasswordTokenValid(false); // Invalidate token after usage
        System.err.println("password : "+user);
        userRepository.delete(user);
        userRepository.save(user);
    }
}