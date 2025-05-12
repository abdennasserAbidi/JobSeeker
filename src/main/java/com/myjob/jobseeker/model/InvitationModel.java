package com.myjob.jobseeker.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InvitationModel {

    private int idInvitation;
    private String message;
    private String description;
    private String typeContract;
    private int idCompany;
    private String companyName;
    private boolean accepted = false;
    //candidate info
    private int idTo;
    private String date;
    private String fullName;
    private String gender;
}
