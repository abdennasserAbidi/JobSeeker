package com.myjob.jobseeker.services;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.myjob.jobseeker.interfaces.IAvisService;
import com.myjob.jobseeker.model.Avis;
import com.myjob.jobseeker.repo.avis.AvisRepository;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
@Service
public class AvisService implements IAvisService {

    private AvisRepository avisRepository;

    @Override
    public void changeAvis(Avis avis) {
        avisRepository.save(avis);
    }

    @Override
    public Page<Avis> getUserAvis(int idCandidate, int page, int size) {
        return avisRepository.findPaginatedAvis(idCandidate, page, size);
    }

    

}
