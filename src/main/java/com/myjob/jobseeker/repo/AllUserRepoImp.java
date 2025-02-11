package com.myjob.jobseeker.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Query;

import com.myjob.jobseeker.model.User;

public class AllUserRepoImp implements AllUserRepo {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Page<User> findPaginatedAllUser(PageRequest pageable) {
    
        // Unwind the experiences array
        Aggregation aggregation = Aggregation.newAggregation(
            Aggregation.unwind("User")
        );
        System.err.println("qqqqqqqqqqqqqqqqqqqqqqqqqqq   "+aggregation);


        // Execute the aggregation
    AggregationResults<User> results = mongoTemplate.aggregate(
        aggregation,
        "User", // The collection name
        User.class // The class type to map the results
    );


    // Count the total number of experiences for the user
    long total = mongoTemplate.count(
        new Query(), // Count experiences for the user
        "User" // The collection name
    );

        return new PageImpl<>(results.getMappedResults(), pageable, total);
    
    }


}
