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

import java.util.List;

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

    @GetMapping("/checkUserLike")
    public ResponseEntity<Boolean> checkUserLike(
            @RequestParam int idAnnounce,
            @RequestParam int idConnected) {

        Boolean isThere = announcementService.getLiked(idAnnounce, idConnected);

        return ResponseEntity.ok(isThere);
    }

    @GetMapping("/checkUserLikeAllPostCompany")
    public ResponseEntity<List<Boolean>> getLikedAllPostCompany(
            @RequestParam int idConnected) {

        List<Boolean> isAllThere = announcementService.getLikedAllPostCompany(idConnected);

        return ResponseEntity.ok(isAllThere);
    }

    @GetMapping("/getNumberLikeAllPostsCompany")
    public ResponseEntity<List<Integer>> getNumberLikeAllPostsCompany(
            @RequestParam int idConnected) {

        List<Integer> isAllThere = announcementService.getNumberLikeAllPostsCompany(idConnected);

        return ResponseEntity.ok(isAllThere);
    }

    @GetMapping("/getNumberCommentAllPostsCompany")
    public ResponseEntity<List<Integer>> getNumberCommentAllPostsCompany(
            @RequestParam int idConnected) {

        List<Integer> isAllThere = announcementService.getNumberCommentAllPostsCompany(idConnected);

        return ResponseEntity.ok(isAllThere);
    }

    @GetMapping("/getCommentAllPostsCompany")
    public ResponseEntity<List<CommentsPost>> getCommentAllPostsCompany(
            @RequestParam int idAnnounce,
            @RequestParam int idConnected) {

        List<CommentsPost> isAllThere = announcementService.getCommentAllPostsCompany(idAnnounce, idConnected);

        return ResponseEntity.ok(isAllThere);
    }

    @GetMapping("/checkUserLikeAllPost")
    public ResponseEntity<List<Boolean>> checkUserLikeAllPost(
            @RequestParam int idConnected) {

        List<Boolean> isAllThere = announcementService.getLikedAllPost(idConnected);

        return ResponseEntity.ok(isAllThere);
    }

    @GetMapping("/getNumberLikeAllPosts")
    public ResponseEntity<List<Integer>> getNumberLikeAllPosts(
            @RequestParam int idConnected) {

        List<Integer> isAllThere = announcementService.getNumberLikeAllPosts(idConnected);

        return ResponseEntity.ok(isAllThere);
    }

    @GetMapping("/getNumberCommentAllPosts")
    public ResponseEntity<List<Integer>> getNumberCommentAllPosts(
            @RequestParam int idConnected) {

        List<Integer> isAllThere = announcementService.getNumberCommentAllPosts(idConnected);

        return ResponseEntity.ok(isAllThere);
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

    @GetMapping("/getAnnouncementsCandidate")
    public ResponseEntity<Page<AnnounceModel>> getCompanyAnnouncementsCandidate(
            @RequestParam int page,
            @RequestParam int size) {

        Page<AnnounceModel> invitation = announcementService.getPaginatedAnnouncementCandidate(page, size);


        return ResponseEntity.ok(invitation);
    }
}