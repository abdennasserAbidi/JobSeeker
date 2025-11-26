package com.myjob.jobseeker.model;

import lombok.Data;

@Data
public class InvitationModel {

    private int idInvitation;
    private String message;
    private String description;
    private String typeContract;
    private String duration;
    private String descriptionContract;
    private int idCompany;
    private String companyName;
    private String nameContract;
    private String status;
    private String updatedStatus;
    private String reason;
    private String dateEnd;
    private boolean accepted = false;
    //candidate info
    private int idTo;
    private String date;
    private String fullName;
    private String gender;

    @Override
    public String toString() {
        return "InvitationModel{" +
                "idInvitation=" + idInvitation +
                ", message='" + message + '\'' +
                ", description='" + description + '\'' +
                ", typeContract='" + typeContract + '\'' +
                ", duration='" + duration + '\'' +
                ", descriptionContract='" + descriptionContract + '\'' +
                ", idCompany=" + idCompany +
                ", companyName='" + companyName + '\'' +
                ", nameContract='" + nameContract + '\'' +
                ", status='" + status + '\'' +
                ", updatedStatus='" + updatedStatus + '\'' +
                ", reason='" + reason + '\'' +
                ", dateEnd='" + dateEnd + '\'' +
                ", accepted=" + accepted +
                ", idTo=" + idTo +
                ", date='" + date + '\'' +
                ", fullName='" + fullName + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }
}
