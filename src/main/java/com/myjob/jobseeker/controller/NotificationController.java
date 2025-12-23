package com.myjob.jobseeker.controller;

import com.myjob.jobseeker.dtos.NotificationMessage;
import com.myjob.jobseeker.dtos.ExperienceResponse;
import com.myjob.jobseeker.interfaces.INotificationService;
import com.myjob.jobseeker.model.NotificationModel;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class NotificationController {

    private final INotificationService notificationService;


    @PostMapping("/updatetoken")
    public ResponseEntity<ExperienceResponse> updateToken(@RequestParam int id, @RequestParam String token) {
        notificationService.updateToken(id, token);
        ExperienceResponse experienceResponse = new ExperienceResponse();
        experienceResponse.setId(1);
        experienceResponse.setMessage("saved successfully");

        return ResponseEntity.ok(experienceResponse);
    }

    @PostMapping("/sendnotification")
    public ResponseEntity<ExperienceResponse> sendNotification(@RequestBody NotificationMessage notificationMessage, String receiverType) {
        String res = notificationService.sendNotification(notificationMessage, receiverType);

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

        Page<NotificationModel> invitation = notificationService.getPaginatedNotification(id, page, size);

        return ResponseEntity.ok(invitation);
    }
}