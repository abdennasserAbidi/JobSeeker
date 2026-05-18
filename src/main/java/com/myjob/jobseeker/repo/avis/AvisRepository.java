package com.myjob.jobseeker.repo.avis;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.myjob.jobseeker.model.Avis;

@Repository
public interface AvisRepository extends MongoRepository<Avis, Integer>, AvisRepositoryCustom {
   List<Avis> findByIdCandidate(int idCandidate);
}
