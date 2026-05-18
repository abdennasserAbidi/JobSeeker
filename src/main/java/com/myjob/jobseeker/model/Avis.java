package com.myjob.jobseeker.model;

import lombok.Data;

@Data
public class Avis {

    private int id;
    private int idCandidate;
    private String candidateName;
    private int idUserDemand;
    private String userDemandName;
    private String like;
    private String note;
    private float percentRate; 

}
