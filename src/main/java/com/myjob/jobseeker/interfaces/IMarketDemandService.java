package com.myjob.jobseeker.interfaces;

import com.myjob.jobseeker.model.MarketDemandModel;

import org.springframework.data.domain.Page;

public interface IMarketDemandService {
    void saveDemand(MarketDemandModel demand);
    void countDownTrial(int idDemand);
    Page<MarketDemandModel> getPaginatedDemands(int page, int size);
    MarketDemandModel getDemand(int idDemand);
    Page<MarketDemandModel> getDemandFiltered(String word, int page, int size);
}
