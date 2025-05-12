package com.myjob.jobseeker.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AnnounceModel {

    private int idAnnounce;
    private String title;
    private String description;
    private String date;
    private int idCompany;
    private String companyName;
    private boolean accepted = false;
}
