package com.myjob.jobseeker.dtos;

import com.myjob.jobseeker.model.InvitationModel;

public class InvitationDto {

    private int idConnected;
    private InvitationModel invitationModel;
    public int getIdConnected() {
        return idConnected;
    }
    public void setIdConnected(int idConnected) {
        this.idConnected = idConnected;
    }
    public InvitationModel getInvitationModel() {
        return invitationModel;
    }
    public void setInvitationModel(InvitationModel invitationModel) {
        this.invitationModel = invitationModel;
    }
}
