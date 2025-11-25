package com.myjob.jobseeker.controller;

import com.myjob.jobseeker.dtos.NotificationMessage;
import com.myjob.jobseeker.dtos.ExperienceResponse;
import com.myjob.jobseeker.model.NotificationModel;
import com.myjob.jobseeker.services.AuthenticationService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
@RestController
@CrossOrigin(origins = "*")
public class NotificationController {

    private final AuthenticationService authenticationService;

    public NotificationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/updatetoken")
    public ResponseEntity<ExperienceResponse> updateToken(@RequestParam int id, @RequestParam String token) {
        authenticationService.updateToken(id, token);
        ExperienceResponse experienceResponse = new ExperienceResponse();
        experienceResponse.setId(1);
        experienceResponse.setMessage("saved successfully");

        return ResponseEntity.ok(experienceResponse);
    }

    @PostMapping("/sendnotification")
    public ResponseEntity<ExperienceResponse> sendNotification(@RequestBody NotificationMessage notificationMessage) {
        String res = authenticationService.sendNotification(notificationMessage);

        ExperienceResponse experienceResponse = new ExperienceResponse();
        experienceResponse.setId(1);
        experienceResponse.setMessage(res);

        return ResponseEntity.ok(experienceResponse);
    }

    @GetMapping("/getCompanyNotifications")
    public ResponseEntity<Page<NotificationModel>> getCompanyNotifications(
            @RequestParam int id,
            @RequestParam int page,
            @RequestParam int size) {

        Page<NotificationModel> invitation = authenticationService.getPaginatedNotification(id, page, size);

        return ResponseEntity.ok(invitation);
    }
}