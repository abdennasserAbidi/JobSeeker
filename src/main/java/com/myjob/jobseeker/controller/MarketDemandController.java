package com.myjob.jobseeker.controller;

import com.myjob.jobseeker.dtos.ExperienceResponse;
import com.myjob.jobseeker.interfaces.IMarketDemandService;
import com.myjob.jobseeker.model.MarketDemandModel;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class MarketDemandController {

    private final IMarketDemandService marketDemandService;

    @GetMapping("/getAllDemands")
    public ResponseEntity<Page<MarketDemandModel>> getAllDemands(
            @RequestParam int page,
            @RequestParam int size
    ) {
        Page<MarketDemandModel> experience = marketDemandService.getPaginatedDemands(page, size);
        return ResponseEntity.ok(experience);
    }

    @PostMapping("/saveDemand")
    public ResponseEntity<ExperienceResponse> saveDemand(
            @RequestBody MarketDemandModel marketDemandModel
    ) {
        marketDemandService.saveDemand(marketDemandModel);
        ExperienceResponse experienceResponse = new ExperienceResponse();
        experienceResponse.setId(1);
        experienceResponse.setMessage("saved successfully");

        return ResponseEntity.ok(experienceResponse);
    }

}
