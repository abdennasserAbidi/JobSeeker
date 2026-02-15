package com.myjob.jobseeker.services;

import com.myjob.jobseeker.dtos.ExperienceResponse;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

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
            System.out.println("rzlkjgrjglgjlrzgrlzjgrglz    "+demand);
            marketDemandRepository.save(demand);
        });
    }

    @Override
    public ExperienceResponse deleteDemand(int demandId) {
        ExperienceResponse experienceResponse = new ExperienceResponse();
        experienceResponse.setId(5211);

        Optional<MarketDemandModel> optionalDemand = marketDemandRepository.findById(demandId);
        String message = "";

        if (optionalDemand.isPresent()) {
            MarketDemandModel demand = optionalDemand.get();
            marketDemandRepository.delete(demand);
            message = "deleted successfully";
        } else {
            message = "demand n'existe pas";
        }
        
        experienceResponse.setMessage(message);

        return experienceResponse;
    }

    @Override
    public Page<MarketDemandModel> getDemandFiltered(String word, int page, int size) {

        List<MarketDemandModel> newDemands = new ArrayList<>();

        AtomicReference<Page<MarketDemandModel>> demandPage = new AtomicReference<>();
        demandPage.set(new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10), 0));


        List<MarketDemandModel> allDemand = marketDemandRepository.findAll();

        for (MarketDemandModel demand : allDemand) {

                boolean nameContains = Pattern.compile(Pattern.quote(word), Pattern.CASE_INSENSITIVE).matcher(demand.getTitle()).find();
                boolean descriptionContains = Pattern.compile(Pattern.quote(word), Pattern.CASE_INSENSITIVE).matcher(demand.getDescription()).find();

                boolean categoryContains = Pattern.compile(Pattern.quote(word), Pattern.CASE_INSENSITIVE).matcher(demand.getCategory().getDisplayName()).find();
                boolean otherategoryContains = Pattern.compile(Pattern.quote(word), Pattern.CASE_INSENSITIVE).matcher(demand.getOtherCategory()).find();
                boolean toolsContains = Pattern.compile(Pattern.quote(word), Pattern.CASE_INSENSITIVE).matcher(demand.getTools().getDisplayName()).find();
                boolean otherToolsContains = Pattern.compile(Pattern.quote(word), Pattern.CASE_INSENSITIVE).matcher(demand.getOtherTools()).find();

                if (nameContains || descriptionContains || categoryContains || otherategoryContains || toolsContains || otherToolsContains) {
                    newDemands.add(demand);
                }
        }

        if (newDemands.isEmpty()) {
                demandPage.set(new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10), 0));
        } else {
                int s = Math.min(size, newDemands.size());

                PageRequest pageable = PageRequest.of(page - 1, s);
                final int start = (int) pageable.getOffset();
                final int end = Math.min((start + pageable.getPageSize()), s);
                demandPage.set(new PageImpl<>(newDemands.subList(start, end), pageable, s));
        }

        return demandPage.get();
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
