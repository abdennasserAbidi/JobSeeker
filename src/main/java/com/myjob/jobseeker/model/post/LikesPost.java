package com.myjob.jobseeker.model.post;

import lombok.Data;

@Data
public class LikesPost {
    private int idLike;
    private int idCandidate;
    private String userName;
}
