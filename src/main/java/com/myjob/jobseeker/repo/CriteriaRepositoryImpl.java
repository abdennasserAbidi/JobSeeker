package com.myjob.jobseeker.repo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import org.springframework.data.mongodb.core.query.Criteria;
import com.myjob.jobseeker.model.User;

public class CriteriaRepositoryImpl implements CriteriaRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<User> searchUsers(com.myjob.jobseeker.dtos.Criteria request) {

        Query query = new Query();

        if (!request.getStatus().isEmpty()) {
            List<String> asector = request.getStatus();
            addCriterias(asector, query, "invitations.status");
        }

        if (!request.getCategories().isEmpty()) {

            /*Criteria criteria = new Criteria().orOperator(
                    Criteria.where("preferredWorkType").is(id),
                    Criteria.where("preferredEmploymentType").is(id)
            );*/

            List<String> asector = request.getCategories();
            addCriterias(asector, query, "preferredWorkType");
        }
        
        if (!request.getExperiences().isEmpty()) {
            List<String> asector = request.getExperiences();
            addCriterias(asector, query, "professionalStatus.userExperience");
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
            query.addCriteria(org.springframework.data.mongodb.core.query.Criteria.where("professionalStatus.workType").in(asector));
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
            addCriterias(asector, query, "professionalStatus.availability");
        }

        return mongoTemplate.find(query, User.class);
    }

    void addCriterias(List<String> crit, Query query, String tag) {
        query.addCriteria(org.springframework.data.mongodb.core.query.Criteria.where(tag).in(crit));
    }

}
