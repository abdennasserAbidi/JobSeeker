package com.myjob.jobseeker.controller;

import com.myjob.jobseeker.dtos.*;
import com.myjob.jobseeker.model.FilterInvitationBody;
import com.myjob.jobseeker.model.InvitationModel;
import com.myjob.jobseeker.model.User;
import com.myjob.jobseeker.services.AuthenticationService;
import org.springframework.data.domain.Page;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/auth")
@RestController
@CrossOrigin(origins = "*")
public class InvitationController {

    private final AuthenticationService authenticationService;

    public InvitationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping("/getCompanyInvitations")
    public ResponseEntity<Page<InvitationModel>> getCompanyInvitations(
            @RequestParam int id,
            @RequestParam int page,
            @RequestParam int size) {

        Page<InvitationModel> invitation = authenticationService.getPaginatedInvitations(id, page, size);

        return ResponseEntity.ok(invitation);
    }

    @PostMapping("/getFilteredInvitation")
    public ResponseEntity<Page<InvitationModel>> getFilteredInvitation(
            @RequestBody FilterInvitationBody request,
            @RequestParam int page,
            @RequestParam int size
    ) {
        Page<InvitationModel> response = authenticationService.getPaginatedInvitationsFiltered(request, page, size);

        return ResponseEntity.ok(response);
    }

    public void sendNotificationAfterSendInvitation(NotificationMessage notificationMessage) {
        String res = authenticationService.sendNotification(notificationMessage);
    }

    @PostMapping("/sendInvitation")
    public ResponseEntity<ExperienceResponse> sendInvitation(@RequestBody InvitationDto invitationDto) {

        authenticationService.sendInvitation(invitationDto.getIdConnected(), invitationDto.getInvitationModel());
        int idInvitation = invitationDto.getInvitationModel().getIdInvitation();
        int idConnected = invitationDto.getIdConnected();

        User user = authenticationService.getUser(idConnected);

        Map<String, String> data = new HashMap<>();
        data.put("idInvitation", idInvitation+"");

        NotificationMessage notificationMessage = new NotificationMessage();
        notificationMessage.setRecipientToken(user.getFcmToken());
        notificationMessage.setData(data);

        sendNotificationAfterSendInvitation(notificationMessage);

        ExperienceResponse experienceResponse = new ExperienceResponse();
        experienceResponse.setId(1);
        experienceResponse.setMessage("saved successfully");

        return ResponseEntity.ok(experienceResponse);
    }

    @GetMapping("/getInvitationById")
    public ResponseEntity<InvitationUser> getInvitationById(
            @RequestParam int idUser,
            @RequestParam int idInvitation) {

        InvitationUser experiences = authenticationService.getInvitationDetail(idUser, idInvitation);

        return ResponseEntity.ok(experiences);
    }

    @PostMapping("/finishProcess")
    public ResponseEntity<InvitationDto> finishProcess(@RequestBody InvitationDto invitationDto) {
        authenticationService.finishProcess(invitationDto.getIdConnected(), invitationDto.getInvitationModel());
        return ResponseEntity.ok(invitationDto);
    }

    @MessageMapping("/invitations/requestInvitations")
    @SendTo("/topic/invitations")
    public Page<InvitationModel> handleRequest(Map<String, Integer> request) {

        int id = request.getOrDefault("id", 0);
        int page = request.getOrDefault("page", 1);
        int size = request.getOrDefault("size", 10);
        return authenticationService.getPaginatedInvitations(id, page, size);
    }

    @GetMapping("/getInvitationDetail")
    public ResponseEntity<InvitationUser> getInvitationDetail(
            @RequestParam int id,
            @RequestParam int idInvitation
    ) {

        InvitationUser invitation = authenticationService.getInvitationDetail(id, idInvitation);

        return ResponseEntity.ok(invitation);
    }

    @GetMapping("/getInvitations")
    public ResponseEntity<Page<InvitationModel>> getInvitationsPages(
            @RequestParam int id,
            @RequestParam int page,
            @RequestParam int size) {

        Page<InvitationModel> experiences = authenticationService.getPaginatedInvitations(id, page, size);

        return ResponseEntity.ok(experiences);
    }

    @GetMapping("/getInvitationsByTag")
    public ResponseEntity<Page<InvitationModel>> getInvitationsByTag(
            @RequestParam int id,
            @RequestParam int page,
            @RequestParam int size) {

        Page<InvitationModel> experiences = authenticationService.getPaginatedInvitationsByContract(id, page, size);

        return ResponseEntity.ok(experiences);
    }

    @GetMapping("/getCompaniesValidated")
    public ResponseEntity<java.util.List<String>> getCompaniesValidated() {

        java.util.List<String> experiences = authenticationService.getCompaniesValidated();
        return ResponseEntity.ok(experiences);
    }

    @PostMapping("/acceptRejectInvitation")
    public ResponseEntity<ExperienceResponse> acceptRejectInvitation(@RequestBody InvitationDto invitationDto) {

        String status = authenticationService.acceptRejectInvitation(invitationDto);

        ExperienceResponse experienceResponse = new ExperienceResponse();
        experienceResponse.setId(1);
        experienceResponse.setMessage(status);

        return ResponseEntity.ok(experienceResponse);
    }

    @PostMapping("/deleteInvitation")
    public ResponseEntity<ExperienceResponse> deleteInvitation(@RequestParam int idInvitation, @RequestParam int idInvitationFrom) {

        authenticationService.deleteInvitation(idInvitation, idInvitationFrom);

        ExperienceResponse experienceResponse = new ExperienceResponse();
        experienceResponse.setId(1);
        experienceResponse.setMessage("removed successfully");

        return ResponseEntity.ok(experienceResponse);
    }

}