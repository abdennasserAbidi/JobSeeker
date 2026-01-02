package com.myjob.jobseeker.model.announces;

import lombok.Data;

import java.util.List;

@Data
public class AnnounceResponse {
    private int idAnnounceResponse;
    private List<AnnounceModel> announceModel;
    private String message;
}
