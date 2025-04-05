package com.myjob.jobseeker.repo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import com.myjob.jobseeker.dtos.Criteria;
import com.myjob.jobseeker.model.User;

public class CriteriaRepositoryImpl implements CriteriaRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<User> searchUsers(Criteria request) {

        Query query = new Query();
        
        if (!request.getExperiences().isEmpty()) {
            query.addCriteria(org.springframework.data.mongodb.core.query.Criteria.where("experiences").is(request.getExperiences()));
        }

        if (!request.getActivitySectors().isEmpty()) {
            query.addCriteria(org.springframework.data.mongodb.core.query.Criteria.where("activitySectors").is(request.getActivitySectors()));
        }
        
        if (!request.getCompanies().isEmpty()) {
            query.addCriteria(org.springframework.data.mongodb.core.query.Criteria.where("companies").is(request.getCompanies()));
        }

        if (!request.getInstitutions().isEmpty()) {
            query.addCriteria(org.springframework.data.mongodb.core.query.Criteria.where("institutions").is(request.getInstitutions()));
        }

        if (!request.getTypeContract().isEmpty()) {
            query.addCriteria(org.springframework.data.mongodb.core.query.Criteria.where("typeContract").is(request.getTypeContract()));
        }

        if (!request.getLocation().isEmpty()) {
            query.addCriteria(org.springframework.data.mongodb.core.query.Criteria.where("location").is(request.getLocation()));
        }
        
        return mongoTemplate.find(query, User.class);
    }

}
