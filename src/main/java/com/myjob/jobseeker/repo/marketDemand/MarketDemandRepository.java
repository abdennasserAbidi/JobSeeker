package com.myjob.jobseeker.repo.marketDemand;

import com.myjob.jobseeker.model.MarketDemandModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketDemandRepository  extends MongoRepository<MarketDemandModel, Integer>, IDemandsRepository {
}

