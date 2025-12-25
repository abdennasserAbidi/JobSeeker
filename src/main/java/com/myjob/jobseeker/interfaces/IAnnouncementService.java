package com.myjob.jobseeker.interfaces;

import com.myjob.jobseeker.model.AnnounceModel;
import com.myjob.jobseeker.model.post.CommentsPost;
import com.myjob.jobseeker.model.post.LikesPost;
import org.springframework.data.domain.Page;

import java.util.List;


public interface IAnnouncementService {
    void makeAnnouncement(int id, AnnounceModel input);
    void addComment(int idAnnounce, CommentsPost commentsPost);
    void addLike(int idAnnounce, LikesPost likesPost);
    void removeLike(int idAnnounce, int idConnected);
    Page<AnnounceModel> getPaginatedAnnouncement(int id, int page, int size);
    AnnounceModel getAnnouncement(int idAnnounce, int idCompany);

    Page<AnnounceModel> getPaginatedAnnouncementCandidate(int page, int size);

    Boolean getLiked(int idAnnounce, int idConnected);

    List<Boolean> getLikedAllPost(int idConnected);

    List<Boolean> getLikedAllPostCompany(int idConnected);

    List<Integer> getNumberLikeAllPostsCompany(int idConnected);

    List<Integer> getNumberCommentAllPostsCompany(int idConnected);

    List<CommentsPost> getCommentAllPostsCompany(int idAnnounce, int idConnected);

    List<Integer> getNumberLikeAllPosts(int idConnected);

    List<Integer> getNumberCommentAllPosts(int idConnected);
}
