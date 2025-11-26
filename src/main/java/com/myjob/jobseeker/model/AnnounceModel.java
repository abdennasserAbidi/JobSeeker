package com.myjob.jobseeker.model;

import com.myjob.jobseeker.model.post.CommentsPost;
import com.myjob.jobseeker.model.post.LikesPost;
import com.myjob.jobseeker.model.post.StatusPost;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Data
public class AnnounceModel {
    private int idAnnounce;
    private String title;
    private String description;
    private String date;
    private int idCompany;
    private String companyName;
    private StatusPost status;
    private List<CommentsPost> comments = new ArrayList<>();
    private List<LikesPost> likes = new ArrayList<>();
}
