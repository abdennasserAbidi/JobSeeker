package com.myjob.jobseeker.model;

import lombok.Data;

import java.util.List;

@Data
public class InvitationPageResponse {
    private List<InvitationModel> data;
    private int page;
    private int totalPages;
}