package com.myjob.jobseeker.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.myjob.jobseeker.model.Candidat;

public class CandidateRepositoryImp implements CandidateRepository {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Page<Candidat> findPaginatedCandidate(String role, int page, int size) {

        long skip = (long) (page - 1) * size;

        // Unwind the experiences array
        Aggregation aggregation = Aggregation.newAggregation(
            Aggregation.match(Criteria.where("role").is(role)), // Match the user by userId
            Aggregation.unwind("candidate"),
            Aggregation.replaceRoot("candidate"),
            Aggregation.skip(skip),
            Aggregation.limit(size)
        );

        // Execute the aggregation
    AggregationResults<Candidat> results = mongoTemplate.aggregate(
        aggregation,
        "User", // The collection name
        Candidat.class // The class type to map the results
    );

    // Count the total number of experiences for the user
    long total = mongoTemplate.count(
        new Query(Criteria.where("role").is(role)), // Count experiences for the user
        "User" // The collection name
    );

        return new PageImpl<>(results.getMappedResults(), PageRequest.of(page, size), total);
    }

}
