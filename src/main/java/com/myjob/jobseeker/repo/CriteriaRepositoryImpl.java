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

        if (!request.getCategories().isEmpty()) {
            query.addCriteria(org.springframework.data.mongodb.core.query.Criteria.where("categories").is(request.getCategories()));
        }
        
        if (!request.getExperiences().isEmpty()) {
            query.addCriteria(org.springframework.data.mongodb.core.query.Criteria.where("experiences").is(request.getExperiences()));
        }

        if (!request.getPreferredActivitySector().isEmpty()) {
            
            List<String> asector = request.getPreferredActivitySector();
            addCriterias(asector, query, "preferredActivitySector");

        }
        
        if (!request.getCompanies().isEmpty()) {

            List<String> asector = request.getCompanies();
            addCriterias(asector, query, "companies");
        }

        if (!request.getInstitutions().isEmpty()) {

            List<String> asector = request.getInstitutions();
            addCriterias(asector, query, "institutions");
        }

        if (!request.getTypeContract().isEmpty()) {

            List<String> asector = request.getTypeContract();
            addCriterias(asector, query, "typeContract");
        }

        if (!request.getLocation().isEmpty()) {
            List<String> asector = request.getLocation();
            addCriterias(asector, query, "address");
        }

        if (!request.getSituation().isEmpty()) {
            List<String> asector = request.getSituation();
            addCriterias(asector, query, "situation");
        }

        if (!request.getSex().isEmpty()) {
            List<String> asector = request.getSex();
            addCriterias(asector, query, "sexe");
        }

        if (!request.getDisponibility().isEmpty()) {
            List<String> asector = request.getDisponibility();
            addCriterias(asector, query, "availability");
        }

        return mongoTemplate.find(query, User.class);
    }

    void addCriterias(List<String> crit, Query query, String tag) {
        for(String x: crit) {
            query.addCriteria(org.springframework.data.mongodb.core.query.Criteria.where(tag).is(x));
        }
    }

}
