package com.myjob.jobseeker.repo;

import org.springframework.data.domain.Page;
import com.myjob.jobseeker.model.Candidat;

public interface CandidateRepository {
   Page<Candidat> findPaginatedCandidate(String role, int page, int size);
}
