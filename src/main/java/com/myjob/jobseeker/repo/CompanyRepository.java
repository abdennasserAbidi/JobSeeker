package com.myjob.jobseeker.repo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.myjob.jobseeker.model.CompanyModel;

@Repository
public interface CompanyRepository extends MongoRepository<CompanyModel, Integer> {

}
