package com.myjob.jobseeker.repo;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.myjob.jobseeker.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, Integer>, ExperienceRepository, EducationRepository, FavoritesRepository {
    Optional<User> findByEmail(String email);
    Page<User> findByRole(String role, Pageable pageable);
}
