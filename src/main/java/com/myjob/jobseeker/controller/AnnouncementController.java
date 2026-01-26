package com.myjob.jobseeker.controller;

import com.myjob.jobseeker.dtos.ExperienceResponse;
import com.myjob.jobseeker.dtos.NotificationMessage;
import com.myjob.jobseeker.interfaces.IAnnouncementService;
import com.myjob.jobseeker.interfaces.INotificationService;
import com.myjob.jobseeker.interfaces.IUserService;
import com.myjob.jobseeker.model.announces.AnnounceModel;
import com.myjob.jobseeker.model.User;
import com.myjob.jobseeker.model.announces.AnnounceResponse;
import com.myjob.jobseeker.model.post.CommentsPost;
import com.myjob.jobseeker.model.post.LikesPost;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/auth")
@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class AnnouncementController {

    private final IAnnouncementService announcementService;
    private final IUserService userService;
    private final INotificationService notificationService;

    @PostMapping("/updateAnnouncement")
    public ResponseEntity<ExperienceResponse> updateAnnouncement(
            @RequestParam int idUserConnected,
            @RequestBody AnnounceModel announceModel) {

        announcementService.updateAnnouncement(idUserConnected, announceModel);

        ExperienceResponse experienceResponse = new ExperienceResponse();
        experienceResponse.setId(1);
        experienceResponse.setMessage("saved successfully");

        return ResponseEntity.ok(experienceResponse);
    }

    @PostMapping("/makeAnnouncement")
    public ResponseEntity<ExperienceResponse> makeAnnouncement(
            @RequestParam int idUserConnected,
            @RequestBody AnnounceModel announceModel) {

        announcementService.makeAnnouncement(idUserConnected, announceModel);

        String title = announceModel.getTitle();
        String description = announceModel.getDescription();
        int idCompany = announceModel.getIdAnnounce();
        String companyName = announceModel.getCompanyName();
        int idAnnounce = announceModel.getIdAnnounce();

        List<User> candidates = userService.getCandidate();

        for (User user : candidates) {
            Map<String, String> data = new HashMap<>();
            data.put("idReceiver", user.getId() + "");
            data.put("username", user.getFullName());
            data.put("idAnnounce", idAnnounce + "");
            data.put("idCompany", idCompany + "");
            data.put("companyName", companyName);
            data.put("title", title);
            data.put("description", description);

            NotificationMessage notificationMessage = new NotificationMessage();
            notificationMessage.setRecipientToken(user.getFcmToken());
            notificationMessage.setTitle(companyName);
            notificationMessage.setBody("Cette entreprise à publé une poste");
            notificationMessage.setData(data);

            System.out.println("ifoezjgjhgoitehgiorhgz      "+notificationMessage);
            sendNotificationAfterPost(notificationMessage);
        }

        ExperienceResponse experienceResponse = new ExperienceResponse();
        experienceResponse.setId(1);
        experienceResponse.setMessage("saved successfully");

        return ResponseEntity.ok(experienceResponse);
    }

    public void sendNotificationAfterPost(NotificationMessage notificationMessage) {
        String res = notificationService.sendNotification(notificationMessage);
    }

    @GetMapping("/getAnnouncement")
    public ResponseEntity<AnnounceModel> getAnnouncement(
            @RequestParam int idAnnounce,
            @RequestParam int idCompany
    ) {

        AnnounceModel announce = announcementService.getAnnouncement(idAnnounce, idCompany);

        return ResponseEntity.ok(announce);
    }

    @GetMapping("/findAnnounceCompany")
    public ResponseEntity<AnnounceResponse> findAnnounceCompany(
            @RequestParam String type,
            @RequestParam int idCompany
    ) {

        AnnounceResponse announce = announcementService.findAnnounceCompany(type, idCompany);

        return ResponseEntity.ok(announce);
    }

    @GetMapping("/findAnnounceCandidate")
    public ResponseEntity<Page<AnnounceModel>> findAnnounceCandidate(
            @RequestParam String type,
            @RequestParam int page,
            @RequestParam int size
    ) {
        Page<AnnounceModel> announce = announcementService.findAnnounceCandidate(type, page, size);

        return ResponseEntity.ok(announce);
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

    @PostMapping("/deletePostCompany")
    public ResponseEntity<ExperienceResponse> deletePostCompany(
            @RequestParam int idAnnounce,
            @RequestParam int idConnected) {

        ExperienceResponse delete = announcementService.deletePostCompany(idAnnounce, idConnected);

        return ResponseEntity.ok(delete);
    }

    @GetMapping("/getCommentAllPostsCompany")
    public ResponseEntity<List<CommentsPost>> getCommentAllPostsCompany(@RequestParam int idAnnounce) {

        List<CommentsPost> isAllThere = announcementService.getCommentAllPostsCompany(idAnnounce);

        return ResponseEntity.ok(isAllThere);
    }

    @GetMapping("/checkUserLikeAllPost")
    public ResponseEntity<List<Boolean>> checkUserLikeAllPost(
            @RequestParam int idConnected) {

        List<Boolean> isAllThere = announcementService.getLikedAllPost(idConnected);

        return ResponseEntity.ok(isAllThere);
    }

    @GetMapping("/getNumberLikeAllPosts")
    public ResponseEntity<List<Integer>> getNumberLikeAllPosts() {

        List<Integer> isAllThere = announcementService.getNumberLikeAllPosts();

        return ResponseEntity.ok(isAllThere);
    }

    @GetMapping("/getNumberCommentAllPosts")
    public ResponseEntity<List<Integer>> getNumberCommentAllPosts() {
        List<Integer> isAllThere = announcementService.getNumberCommentAllPosts();
        return ResponseEntity.ok(isAllThere);
    }

    @GetMapping("/getNumberCommentAllPostsCompany")
    public ResponseEntity<List<Integer>> getNumberCommentAllPostsCompany(
            @RequestParam int idConnected) {

        List<Integer> isAllThere = announcementService.getNumberCommentAllPostsCompany(idConnected);

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