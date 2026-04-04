package com.myjob.jobseeker.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.myjob.jobseeker.model.field.FieldModel;

@Repository
public interface FieldsRepository extends MongoRepository<FieldModel, Integer> {

}
