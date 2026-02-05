package com.myjob.jobseeker.services;

import com.myjob.jobseeker.interfaces.IMarketDemandService;
import com.myjob.jobseeker.model.MarketDemandModel;
import com.myjob.jobseeker.model.User;
import com.myjob.jobseeker.repo.UserRepository;
import com.myjob.jobseeker.repo.marketDemand.MarketDemandRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Data
@AllArgsConstructor
@Service
public class MarketDemandService implements IMarketDemandService {

    private final UserRepository userRepository;
    private final MarketDemandRepository marketDemandRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void saveDemand(MarketDemandModel demand) {
        Optional<User> optionalUser = userRepository.findById(demand.getIdSender());
        optionalUser.ifPresent(user -> {
            demand.setUserSender(user);
            marketDemandRepository.save(demand);
        });
    }

    @Override
    public MarketDemandModel getDemand(int idDemand) {
        return marketDemandRepository
            .findById(idDemand)
            .orElse(new MarketDemandModel());
    }

    @Override
    public void countDownTrial(int idDemand) {
        Optional<MarketDemandModel> optionalDemand = marketDemandRepository.findById(idDemand);
        optionalDemand.ifPresent(demand -> {
            int countTrial = demand.getCountTrial();
            countTrial -= 1;
            demand.setCountTrial(countTrial);
            marketDemandRepository.save(demand);
        });
    }

    @Override
    public Page<MarketDemandModel> getPaginatedDemands(int page, int size) {
        List<MarketDemandModel> list = marketDemandRepository.findAll();
        PageRequest pageable = PageRequest.of(page - 1, size);
        final int start = (int) pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), list.size());

        Page<MarketDemandModel> pager;

        if (start < list.size() && start < end) {
            pager = new PageImpl<>(list.subList(start, end), pageable, list.size());
        } else pager = new PageImpl<>(Collections.emptyList(), pageable, list.size());

        return pager;
    }
}
