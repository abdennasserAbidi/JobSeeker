package com.myjob.jobseeker.repo;

import com.myjob.jobseeker.model.FilterInvitationBody;
import com.myjob.jobseeker.model.User;
import com.myjob.jobseeker.model.datalist.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.myjob.jobseeker.model.InvitationModel;

import java.util.List;

public class InvitationRepositoryImpl implements InvitationRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Page<InvitationModel> findPaginatedInvitations(int id, int page, int size) {


        long skip = (long) (page - 1) * size;

        // Unwind the experiences array
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("_id").is(id)),
                Aggregation.unwind("invitations"),
                //Aggregation.match(Criteria.where("invitations.typeContract").is("Contract")), // Filter inside invitations
                Aggregation.replaceRoot("invitations"),
                Aggregation.skip(skip),
                Aggregation.limit(size)
        );

        // Execute the aggregation
        AggregationResults<InvitationModel> results = mongoTemplate.aggregate(
                aggregation,
                "User", // The collection name
                InvitationModel.class // The class type to map the results
        );

        // Count the total number of experiences for the user
        long total = mongoTemplate.count(
                new Query(Criteria.where("_id").is(id)), // Count experiences for the user
                "User" // The collection name
        );

        return new PageImpl<>(results.getMappedResults(), PageRequest.of(page, size), total);

    }

    @Override
    public Page<InvitationModel> findPaginatedInvitationsByContract(int id, int page, int size) {

        long skip = (long) (page - 1) * size;

        Aggregation aggregation;

        // Unwind the experiences array
        aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("_id").is(id)), // Match the user by userId
                Aggregation.unwind("invitations"),
                Aggregation.match(Criteria.where("invitations.typeContract").is("AUTRE")), // Filter inside invitations
                Aggregation.replaceRoot("invitations"),
                Aggregation.skip(skip),
                Aggregation.limit(size)
        );

        // Execute the aggregation
        AggregationResults<InvitationModel> results = mongoTemplate.aggregate(
                aggregation,
                "User", // The collection name
                InvitationModel.class // The class type to map the results
        );

        // Count the total number of experiences for the user
        long total = mongoTemplate.count(
                new Query(Criteria.where("_id").is(id)), // Count experiences for the user
                "User" // The collection name
        );

        return new PageImpl<>(results.getMappedResults(), PageRequest.of(page, size), total);
    }

    @Override
    public Page<InvitationModel> findPaginatedInvitationsByTag(FilterInvitationBody request, int page, int size) {

        long skip = (long) (page - 1) * size;

        Query query = new Query();


        Aggregation aggregation;


        query.addCriteria(org.springframework.data.mongodb.core.query.Criteria.where("_id").is(request.getIdUser()));


        Criteria allCriteria = new Criteria();

        List<String> asector = request.getListTypeContract();

        boolean errorType = request.getType().isEmpty() || request.getType().equals("All Candidates");
        boolean errorContract = asector.isEmpty();

        if (!errorType || !errorContract) {
            System.out.println("klgtrjnrefjrfjfer    "+request.getType());
            allCriteria.orOperator(
                    Criteria.where("invitations.status").is(request.getType()),
                    Criteria.where("invitations.typeContract").in(asector),
                    Criteria.where("invitations.nameContract").in(asector)
            );
        }


        // Unwind the experiences array
        aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("_id").is(request.getIdUser())), // Match the user by userId
                Aggregation.unwind("invitations"),
                Aggregation.match(allCriteria),
                Aggregation.replaceRoot("invitations"),
                Aggregation.skip(skip),
                Aggregation.limit(size)
        );

        // Execute the aggregation
        AggregationResults<InvitationModel> results = mongoTemplate.aggregate(
                aggregation,
                "User", // The collection name
                InvitationModel.class // The class type to map the results
        );

        // Count the total number of experiences for the user
        long total = mongoTemplate.count(
                new Query(Criteria.where("_id").is(request.getIdUser())), // Count experiences for the user
                "User"
        );

        return new PageImpl<>(results.getMappedResults(), PageRequest.of(page, size), total);
    }
}
