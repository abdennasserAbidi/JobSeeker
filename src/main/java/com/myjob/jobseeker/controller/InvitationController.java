package com.myjob.jobseeker.controller;

import com.myjob.jobseeker.dtos.*;
import com.myjob.jobseeker.interfaces.IInvitationService;
import com.myjob.jobseeker.interfaces.INotificationService;
import com.myjob.jobseeker.interfaces.IUserService;
import com.myjob.jobseeker.model.FilterInvitationBody;
import com.myjob.jobseeker.model.InvitationModel;
import com.myjob.jobseeker.model.User;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/auth")
@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class InvitationController {

    private final IInvitationService invitationService;
    private final INotificationService notificationService;
    private final IUserService userService;


    @GetMapping("/getCompanyInvitations")
    public ResponseEntity<Page<InvitationModel>> getCompanyInvitations(
            @RequestParam int id,
            @RequestParam int page,
            @RequestParam int size) {

        Page<InvitationModel> invitation = invitationService.getPaginatedInvitations(id, page, size);

        return ResponseEntity.ok(invitation);
    }

    @PostMapping("/getFilteredInvitation")
    public ResponseEntity<Page<InvitationModel>> getFilteredInvitation(
            @RequestBody FilterInvitationBody request,
            @RequestParam int page,
            @RequestParam int size
    ) {
        Page<InvitationModel> response = invitationService.getPaginatedInvitationsFiltered(request, page, size);

        return ResponseEntity.ok(response);
    }

    public void sendNotificationAfterSendInvitation(NotificationMessage notificationMessage, String receiverType) {
        String res = notificationService.sendNotification(notificationMessage, receiverType);
    }

    @PostMapping("/sendInvitation")
    public ResponseEntity<ExperienceResponse> sendInvitation(@RequestBody InvitationDto invitationDto) {

        invitationService.sendInvitation(invitationDto.getIdConnected(), invitationDto.getInvitationModel());

        int idInvitation = invitationDto.getInvitationModel().getIdInvitation();
        int idReceiver = invitationDto.getInvitationModel().getIdTo();
        String username = invitationDto.getInvitationModel().getFullName();
        int idCompany = invitationDto.getInvitationModel().getIdCompany();
        String companyName = invitationDto.getInvitationModel().getCompanyName();
        int idConnected = invitationDto.getIdConnected();

        UserResponse user = userService.getUser(idReceiver);

        if (user.getMessage().isEmpty()) {
            Map<String, String> data = new HashMap<>();
            data.put("idInvitation", idInvitation + "");
            data.put("idCompany", idCompany + "");
            data.put("companyName", companyName);
            data.put("idReceiver", idReceiver + "");
            data.put("username", username);

            NotificationMessage notificationMessage = new NotificationMessage();
            notificationMessage.setRecipientToken(user.getUser().getFcmToken());
            notificationMessage.setTitle(companyName);
            notificationMessage.setBody("Cette entreprise vous a envoyé une invitation");
            notificationMessage.setData(data);

            sendNotificationAfterSendInvitation(notificationMessage, "candidate");
        }

        ExperienceResponse experienceResponse = new ExperienceResponse();
        experienceResponse.setId(1);
        experienceResponse.setMessage("saved successfully");

        return ResponseEntity.ok(experienceResponse);
    }

    @GetMapping("/getInvitationById")
    public ResponseEntity<InvitationUser> getInvitationById(
            @RequestParam int idUser,
            @RequestParam int idInvitation) {

        InvitationUser experiences = invitationService.getInvitationDetail(idUser, idInvitation);

        return ResponseEntity.ok(experiences);
    }

    @PostMapping("/finishProcess")
    public ResponseEntity<InvitationDto> finishProcess(@RequestBody InvitationDto invitationDto) {
        invitationService.finishProcess(invitationDto.getIdConnected(), invitationDto.getInvitationModel());

        int idInvitation = invitationDto.getInvitationModel().getIdInvitation();
        int idReceiver = invitationDto.getInvitationModel().getIdTo();
        String username = invitationDto.getInvitationModel().getFullName();
        int idCompany = invitationDto.getInvitationModel().getIdCompany();
        String companyName = invitationDto.getInvitationModel().getCompanyName();
        String status = invitationDto.getInvitationModel().getStatus();
        int idConnected = invitationDto.getIdConnected();

        UserResponse user = userService.getUser(idReceiver);

        if (user.getMessage().isEmpty()) {
            Map<String, String> data = new HashMap<>();
            data.put("idInvitation", idInvitation + "");
            data.put("idCompany", idCompany + "");
            data.put("companyName", companyName);
            data.put("idReceiver", idReceiver + "");
            data.put("username", username);

            NotificationMessage notificationMessage = new NotificationMessage();
            notificationMessage.setRecipientToken(user.getUser().getFcmToken());

            notificationMessage.setTitle(companyName);
            if (status.equals("HIRED")) notificationMessage.setBody("L'entreprise vous a embauché");
            else notificationMessage.setBody("L'entreprise a rejeté votre candidature");
            notificationMessage.setData(data);

            sendNotificationAfterSendInvitation(notificationMessage, "candidate");
        }

        return ResponseEntity.ok(invitationDto);
    }

    @MessageMapping("/invitations/requestInvitations")
    @SendTo("/topic/invitations")
    public Page<InvitationModel> handleRequest(Map<String, Integer> request) {

        int id = request.getOrDefault("id", 0);
        int page = request.getOrDefault("page", 1);
        int size = request.getOrDefault("size", 10);
        return invitationService.getPaginatedInvitations(id, page, size);
    }

    @GetMapping("/getInvitationDetail")
    public ResponseEntity<InvitationUser> getInvitationDetail(
            @RequestParam int id,
            @RequestParam int idInvitation
    ) {

        InvitationUser invitation = invitationService.getInvitationDetail(id, idInvitation);

        return ResponseEntity.ok(invitation);
    }

    @GetMapping("/getInvitations")
    public ResponseEntity<Page<InvitationModel>> getInvitationsPages(
            @RequestParam int id,
            @RequestParam int page,
            @RequestParam int size) {

        Page<InvitationModel> experiences = invitationService.getPaginatedInvitations(id, page, size);

        return ResponseEntity.ok(experiences);
    }

    @GetMapping("/getInvitationsByTag")
    public ResponseEntity<Page<InvitationModel>> getInvitationsByTag(
            @RequestParam int id,
            @RequestParam int page,
            @RequestParam int size) {

        Page<InvitationModel> experiences = invitationService.getPaginatedInvitationsByContract(id, page, size);

        return ResponseEntity.ok(experiences);
    }

    @GetMapping("/getCompaniesValidated")
    public ResponseEntity<java.util.List<String>> getCompaniesValidated() {

        java.util.List<String> experiences = invitationService.getCompaniesValidated();
        return ResponseEntity.ok(experiences);
    }

    @GetMapping("/getInstitutesValidated")
    public ResponseEntity<java.util.List<String>> getInstitutesValidated() {

        java.util.List<String> experiences = invitationService.getInstitutesValidated();
        return ResponseEntity.ok(experiences);
    }

    @PostMapping("/acceptRejectInvitation")
    public ResponseEntity<ExperienceResponse> acceptRejectInvitation(@RequestBody InvitationDto invitationDto) {

        String status = invitationService.acceptRejectInvitation(invitationDto);

        int idInvitation = invitationDto.getInvitationModel().getIdInvitation();
        int idReceiver = invitationDto.getInvitationModel().getIdCompany();
        String companyName = invitationDto.getInvitationModel().getCompanyName();
        int idCandidate = invitationDto.getInvitationModel().getIdTo();
        String username = invitationDto.getInvitationModel().getFullName();
        int idConnected = invitationDto.getIdConnected();

        UserResponse user = userService.getUser(idReceiver);

        if (user.getMessage().isEmpty()) {
            Map<String, String> data = new HashMap<>();
            data.put("idInvitation", idInvitation + "");
            data.put("idCandidate", idCandidate + "");
            data.put("companyName", companyName);
            data.put("idReceiver", idReceiver + "");
            data.put("username", username);

            NotificationMessage notificationMessage = new NotificationMessage();
            notificationMessage.setRecipientToken(user.getUser().getFcmToken());

            notificationMessage.setTitle(username);
            if (status.equals("IN_PROCESS")) notificationMessage.setBody("Ce candidat a accepté votre invitation");
            else notificationMessage.setBody("Ce candidat a refusé votre une invitation");

            notificationMessage.setData(data);

            System.out.println("ftreeeeeeeeee  model   " + notificationMessage);

            sendNotificationAfterSendInvitation(notificationMessage, "company");
        }

        ExperienceResponse experienceResponse = new ExperienceResponse();
        experienceResponse.setId(1);
        experienceResponse.setMessage(status);

        return ResponseEntity.ok(experienceResponse);
    }

    @PostMapping("/deleteInvitation")
    public ResponseEntity<ExperienceResponse> deleteInvitation(@RequestParam int idInvitation, @RequestParam int idInvitationFrom) {

        invitationService.deleteInvitation(idInvitation, idInvitationFrom);

        ExperienceResponse experienceResponse = new ExperienceResponse();
        experienceResponse.setId(1);
        experienceResponse.setMessage("removed successfully");

        return ResponseEntity.ok(experienceResponse);
    }

}