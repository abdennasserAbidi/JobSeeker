package com.myjob.jobseeker.repo;

import java.util.List;

import com.myjob.jobseeker.model.announces.AnnounceModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Query;

import org.springframework.data.mongodb.core.query.Criteria;

import com.myjob.jobseeker.model.CategoryModel;
import com.myjob.jobseeker.model.User;

public class CriteriaRepositoryImpl implements CriteriaRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<User> searchUserSerice(CategoryModel request) {

        Query query = new Query();

        if (!request.getListSector().isEmpty()) {
            query.addCriteria(org.springframework.data.mongodb.core.query.Criteria.where("freelanceSector").in(request.getListSector()));
        }

        if (!request.getListService().isEmpty()) {
            query.addCriteria(org.springframework.data.mongodb.core.query.Criteria.where("freelanceService").in(request.getListService()));
        }
        
        return mongoTemplate.find(query, User.class);
    }



    @Override
    public List<User> searchUsers(com.myjob.jobseeker.dtos.Criteria request) {

        Query query = new Query();
        String lang = request.getLanguage();

        if (!request.getStatus().isEmpty()) {
            List<String> asector = request.getStatus();
            addCriterias(asector, query, "invitations.status");
        }

        if (!request.getCategories().isEmpty()) {
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
            List<String> situation = request.getSituation();
            String fieldToFilter;
            if ("Français".equalsIgnoreCase(lang) || "French".equalsIgnoreCase(lang)) {
                fieldToFilter = "situation.situationFr";
            } else {
                fieldToFilter = "situation.situationEng";
            }
            addCriterias(situation, query, fieldToFilter);
        }

        if (!request.getSex().isEmpty()) {
            List<String> asector = request.getSex();
            String fieldToFilter;
            if ("Français".equalsIgnoreCase(lang) || "French".equalsIgnoreCase(lang)) {
                fieldToFilter = "sexe.genderFr";
            } else {
                fieldToFilter = "sexe.genderEng";
            }
            addCriterias(asector, query, fieldToFilter);
        }

        if (!request.getDisponibility().isEmpty()) {
            List<String> asector = request.getDisponibility();
            addCriterias(asector, query, "professionalStatus.availability");
        }

        return mongoTemplate.find(query, User.class);
    }

    @Override
    public List<AnnounceModel> getComments(int idAnnounce) {

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("announces.idAnnounce").is(idAnnounce)),
                Aggregation.unwind("announces"),
                Aggregation.replaceRoot("announces")
        );

        AggregationResults<AnnounceModel> results = mongoTemplate.aggregate(
                aggregation,
                "User",
                AnnounceModel.class
        );

        return results.getMappedResults();
    }

    void addCriterias(List<String> crit, Query query, String tag) {
        query.addCriteria(org.springframework.data.mongodb.core.query.Criteria.where(tag).in(crit));
    }

}
