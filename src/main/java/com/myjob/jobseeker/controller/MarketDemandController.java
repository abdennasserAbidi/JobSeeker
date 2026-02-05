package com.myjob.jobseeker.controller;

import com.myjob.jobseeker.dtos.ExperienceResponse;
import com.myjob.jobseeker.dtos.NotificationMessage;
import com.myjob.jobseeker.dtos.UserResponse;
import com.myjob.jobseeker.interfaces.IMarketDemandService;
import com.myjob.jobseeker.interfaces.INotificationService;
import com.myjob.jobseeker.interfaces.IUserService;
import com.myjob.jobseeker.model.MarketDemandModel;
import com.myjob.jobseeker.model.User;

import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class MarketDemandController {

    private final IMarketDemandService marketDemandService;
    private final IUserService userService;
    private final INotificationService notificationService;

    @GetMapping("/getAllDemands")
    public ResponseEntity<Page<MarketDemandModel>> getAllDemands(
            @RequestParam int page,
            @RequestParam int size
    ) {
        Page<MarketDemandModel> experience = marketDemandService.getPaginatedDemands(page, size);
        return ResponseEntity.ok(experience);
    }

    @GetMapping("/getDemand")
    public ResponseEntity<MarketDemandModel> getDemand(
            @RequestParam int idDemand
    ) {

        MarketDemandModel demand = marketDemandService.getDemand(idDemand);

        return ResponseEntity.ok(demand);
    }

    @PostMapping("/saveDemand")
    public ResponseEntity<ExperienceResponse> saveDemand(
            @RequestBody MarketDemandModel marketDemandModel
    ) {
        marketDemandService.saveDemand(marketDemandModel);


        String title = marketDemandModel.getTitle();
        String description = marketDemandModel.getDescription();
        int id = marketDemandModel.getIdSender();
        UserResponse sender = userService.getUser(id);

        String nameHoster = "";
        if (sender.getUser().getRole().equals("Candidate") || sender.getUser().getRole().equals("Candidat")) {
            nameHoster = sender.getUser().getFullName();
        } else nameHoster = sender.getUser().getCompanyName();

        List<User> candidates = userService.getAllUser();

        for (User user : candidates) {
            System.out.println("gzlghrzgjgzrlgrz   "+user.getFcmToken());
            Map<String, String> data = new HashMap<>();
            data.put("idDemand", marketDemandModel.getId() + "");
            data.put("idHoster", id + "");
            data.put("nameHoster", nameHoster);
            data.put("title", title);
            data.put("description", description);

            NotificationMessage notificationMessage = new NotificationMessage();
            notificationMessage.setRecipientToken(user.getFcmToken());
            notificationMessage.setTitle("demande de service");
            notificationMessage.setBody(description);
            notificationMessage.setData(data);

            notificationService.sendNotification(notificationMessage);
        }

        ExperienceResponse experienceResponse = new ExperienceResponse();
        experienceResponse.setId(1);
        experienceResponse.setMessage("saved successfully");

        return ResponseEntity.ok(experienceResponse);
    }

    @PostMapping("/countDownTrial")
    public ResponseEntity<ExperienceResponse> countDownTrial(@RequestParam int idDemand) {
        marketDemandService.countDownTrial(idDemand);
        ExperienceResponse experienceResponse = new ExperienceResponse();
        experienceResponse.setId(1);
        experienceResponse.setMessage("saved successfully");

        return ResponseEntity.ok(experienceResponse);
    }

}
