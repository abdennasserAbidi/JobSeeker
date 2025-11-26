package com.myjob.jobseeker.controller;

import com.myjob.jobseeker.dtos.ExperienceResponse;
import com.myjob.jobseeker.interfaces.IAnnouncementService;
import com.myjob.jobseeker.model.AnnounceModel;
import com.myjob.jobseeker.model.post.CommentsPost;
import com.myjob.jobseeker.model.post.LikesPost;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class AnnouncementController {

    private final IAnnouncementService announcementService;


    @PostMapping("/makeAnnouncement")
    public ResponseEntity<ExperienceResponse> makeAnnouncement(
            @RequestParam int idUserConnected,
            @RequestBody AnnounceModel announceModel) {

        announcementService.makeAnnouncement(idUserConnected, announceModel);

        ExperienceResponse experienceResponse = new ExperienceResponse();
        experienceResponse.setId(1);
        experienceResponse.setMessage("saved successfully");

        return ResponseEntity.ok(experienceResponse);
    }

    @PostMapping("/addComment")
    public ResponseEntity<ExperienceResponse> addComment(
            @RequestParam int idAnnounce,
            @RequestBody CommentsPost commentsPost) {

        announcementService.addComment(idAnnounce, commentsPost);

        ExperienceResponse experienceResponse = new ExperienceResponse();
        experienceResponse.setId(1);
        experienceResponse.setMessage("saved successfully");

        return ResponseEntity.ok(experienceResponse);
    }

    @PostMapping("/addLikes")
    public ResponseEntity<ExperienceResponse> addLikes(
            @RequestParam int idAnnounce,
            @RequestBody LikesPost likesPost) {

        announcementService.addLike(idAnnounce, likesPost);

        ExperienceResponse experienceResponse = new ExperienceResponse();
        experienceResponse.setId(1);
        experienceResponse.setMessage("saved successfully");

        return ResponseEntity.ok(experienceResponse);
    }

    @PostMapping("/removeLike")
    public ResponseEntity<ExperienceResponse> removeLike(
            @RequestParam int idAnnounce,
            @RequestParam int idConnected) {

        announcementService.removeLike(idAnnounce, idConnected);

        ExperienceResponse experienceResponse = new ExperienceResponse();
        experienceResponse.setId(1);
        experienceResponse.setMessage("saved successfully");

        return ResponseEntity.ok(experienceResponse);
    }

    @GetMapping("/getCompanyAnnouncements")
    public ResponseEntity<Page<AnnounceModel>> getCompanyAnnouncements(
            @RequestParam int id,
            @RequestParam int page,
            @RequestParam int size) {

        Page<AnnounceModel> invitation = announcementService.getPaginatedAnnouncement(id, page, size);

        return ResponseEntity.ok(invitation);
    }
}