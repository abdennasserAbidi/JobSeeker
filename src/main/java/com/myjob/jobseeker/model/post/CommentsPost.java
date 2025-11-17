package com.myjob.jobseeker.model.post;

import lombok.Data;

@Data
public class CommentsPost {
    private int idComment;
    private int idCandidate;
    private String text;
    private String userName;
}
