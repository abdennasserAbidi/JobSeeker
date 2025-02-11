package com.myjob.jobseeker.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.myjob.jobseeker.model.PasswordResetToken;

@Repository
public interface PasswordResetTokenRepository extends MongoRepository<PasswordResetToken, Integer> {
    
    // Find a PasswordResetToken by its token
    PasswordResetToken findByToken(String token);
    
    // Optionally, you can delete the token after it's used or expired
    void deleteByToken(String token);
}