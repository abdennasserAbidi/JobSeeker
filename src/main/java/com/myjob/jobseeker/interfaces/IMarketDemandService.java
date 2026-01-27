package com.myjob.jobseeker.interfaces;

import com.myjob.jobseeker.model.MarketDemandModel;
import org.springframework.data.domain.Page;

public interface IMarketDemandService {
    void saveDemand(MarketDemandModel demand);
    Page<MarketDemandModel> getPaginatedDemands(int page, int size);
}
