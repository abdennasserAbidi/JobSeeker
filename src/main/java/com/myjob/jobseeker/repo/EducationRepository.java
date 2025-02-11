package com.myjob.jobseeker.repo;

import org.springframework.data.domain.Page;

import com.myjob.jobseeker.model.Education;

public interface EducationRepository {
    Page<Education> findPaginatedEducations(int id, int page, int size);
}
