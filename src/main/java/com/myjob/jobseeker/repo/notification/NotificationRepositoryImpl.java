package com.myjob.jobseeker.repo.notification;

import com.myjob.jobseeker.model.NotificationModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

public class NotificationRepositoryImpl implements NotificationRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Page<NotificationModel> findPaginatedNotification(int userId, int page, int size) {

        long skip = (long) (page - 1) * size;

        // Unwind the experiences array
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("idSender").ne(userId)),
                Aggregation.skip(skip),
                Aggregation.limit(size)
        );

        // Execute the aggregation
        AggregationResults<NotificationModel> results = mongoTemplate.aggregate(
                aggregation,
                "notifications", // The collection name
                NotificationModel.class // The class type to map the results
        );

        long total = mongoTemplate.count(
                new Query(Criteria.where("idSender").ne(userId)),
                "notifications"
        );

        return new PageImpl<>(results.getMappedResults(), PageRequest.of(page, size), total);
    }
}
