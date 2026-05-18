package com.myjob.jobseeker.repo.avis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import com.myjob.jobseeker.model.Avis;

public class AvisRepositoryCustomImpl implements AvisRepositoryCustom {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Page<Avis> findPaginatedAvis(int idCandidat, int page, int size) {
        long skip = (long) (page - 1) * size;
        System.out.println("gjrkgnjkrgrzg  size  "+page); 
        // Unwind the experiences array
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("idCandidate").is(idCandidat)),
                Aggregation.skip(skip),
                Aggregation.limit(size)
        );

        // Execute the aggregation
        AggregationResults<Avis> results = mongoTemplate.aggregate(
                aggregation,
                "avis", // The collection name
                Avis.class // The class type to map the results
        );

        // Count the total number of experiences for the user
        long total = mongoTemplate.count(
                new Query(Criteria.where("idCandidate").is(idCandidat)), // Count experiences for the user
                "avis" // The collection name
        );

        System.out.println("gjrkgnjkrgrzg  gkletjtkejhjtekhek  "+results.getMappedResults());        


        return new PageImpl<>(results.getMappedResults(), PageRequest.of(page, size), total);
    }
}

