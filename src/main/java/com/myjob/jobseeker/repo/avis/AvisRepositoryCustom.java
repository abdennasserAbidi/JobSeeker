package com.myjob.jobseeker.repo.avis;

import org.springframework.data.domain.Page;
import com.myjob.jobseeker.model.Avis;

public interface AvisRepositoryCustom {
   Page<Avis> findPaginatedAvis(int idCandidat, int page, int size);  
}
