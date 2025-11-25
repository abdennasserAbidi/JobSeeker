package com.myjob.jobseeker.interfaces;

import com.myjob.jobseeker.model.AnnounceModel;
import com.myjob.jobseeker.model.post.CommentsPost;
import com.myjob.jobseeker.model.post.LikesPost;
import org.springframework.data.domain.Page;


public interface IAnnouncementService {
    void makeAnnouncement(int id, AnnounceModel input);
    void addComment(int idAnnounce, CommentsPost commentsPost);
    void addLike(int idAnnounce, LikesPost likesPost);
    void removeLike(int idAnnounce, int idConnected);
    Page<AnnounceModel> getPaginatedAnnouncement(int id, int page, int size);
}
