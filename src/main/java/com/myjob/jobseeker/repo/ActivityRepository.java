package com.myjob.jobseeker.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.myjob.jobseeker.model.activity.ActivityModel;

@Repository
public interface ActivityRepository extends MongoRepository<ActivityModel, Integer> {

}