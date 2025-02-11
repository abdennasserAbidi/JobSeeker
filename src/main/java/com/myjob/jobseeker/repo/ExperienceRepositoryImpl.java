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

import com.myjob.jobseeker.model.Experience;

public class ExperienceRepositoryImpl implements ExperienceRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Page<Experience> findPaginatedExperiences(int userId, int page, int size) {

        long skip = (long) (page - 1) * size;

        // Unwind the experiences array
        Aggregation aggregation = Aggregation.newAggregation(
            Aggregation.match(Criteria.where("_id").is(userId)), // Match the user by userId
            Aggregation.unwind("experiences"),
            Aggregation.replaceRoot("experiences"),
            Aggregation.skip(skip),
            Aggregation.limit(size)
        );

        // Execute the aggregation
    AggregationResults<Experience> results = mongoTemplate.aggregate(
        aggregation,
        "User", // The collection name
        Experience.class // The class type to map the results
    );

    // Count the total number of experiences for the user
    long total = mongoTemplate.count(
        new Query(Criteria.where("_id").is(userId)), // Count experiences for the user
        "User" // The collection name
    );

        return new PageImpl<>(results.getMappedResults(), PageRequest.of(page, size), total);
    }
}
