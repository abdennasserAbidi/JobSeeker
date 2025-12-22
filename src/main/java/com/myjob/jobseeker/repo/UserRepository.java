package com.myjob.jobseeker.repo;

import com.myjob.jobseeker.model.User;
import com.myjob.jobseeker.repo.announcement.AnnouncementRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, Integer>, CriteriaRepository, ExperienceRepository, EducationRepository, FavoritesRepository, InvitationRepository, SearchRepository, AnnouncementRepository, StoredFileRepository {
    Optional<User> findByEmail(String email);
    List<User> findByRole(String role);
}
