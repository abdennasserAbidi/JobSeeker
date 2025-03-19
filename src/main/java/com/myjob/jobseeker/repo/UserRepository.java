package com.myjob.jobseeker.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.myjob.jobseeker.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, Integer>, ExperienceRepository, EducationRepository, FavoritesRepository, InvitationRepository {
    Optional<User> findByEmail(String email);
    List<User> findByRole(String role);
}
