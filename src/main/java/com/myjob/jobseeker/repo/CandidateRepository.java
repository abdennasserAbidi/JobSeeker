package com.myjob.jobseeker.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.myjob.jobseeker.model.Candidat;
import com.myjob.jobseeker.model.User;

public interface CandidateRepository {
   Page<Candidat> findPaginatedCandidate(String role, int page, int size);
}
