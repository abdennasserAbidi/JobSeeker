package com.myjob.jobseeker.repo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.myjob.jobseeker.model.institute.InstituteModel;

@Repository
public interface InstituteRepository extends MongoRepository<InstituteModel, Integer> {

}
