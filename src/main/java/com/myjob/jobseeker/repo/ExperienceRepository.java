package com.myjob.jobseeker.repo;

import org.springframework.data.domain.Page;

import com.myjob.jobseeker.model.Experience;

public interface ExperienceRepository {
    Page<Experience> findPaginatedExperiences(int id, int page, int size);
}
