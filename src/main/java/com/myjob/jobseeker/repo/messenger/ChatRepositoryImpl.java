package com.myjob.jobseeker.repo.messenger;

import com.myjob.jobseeker.model.chat.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

public class ChatRepositoryImpl implements ChatRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Page<ChatModel> findPaginatedMessages(int userId, int page, int size) {

        long skip = (long) (page - 1) * size;

        // Unwind the experiences array
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("_id").is(userId)),
                Aggregation.unwind("messages"),
                Aggregation.replaceRoot("messages"),
                Aggregation.skip(skip),
                Aggregation.limit(size)
        );

        // Execute the aggregation
        AggregationResults<ChatModel> results = mongoTemplate.aggregate(
                aggregation,
                "User",
                ChatModel.class
        );

        long total = mongoTemplate.count(
                new Query(Criteria.where("_id").is(userId)),
                "User"
        );

        return new PageImpl<>(results.getMappedResults(), PageRequest.of(page, size), total);
    }
}
