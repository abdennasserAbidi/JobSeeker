package com.myjob.jobseeker.model;

public class InvitationModel {

    private int idInvitation;
    private int idFrom;
    private int idTo;
    private String message;
    private String typeContract;
    
    public int getIdInvitation() {
        return idInvitation;
    }
    public void setIdInvitation(int idInvitation) {
        this.idInvitation = idInvitation;
    }
    public int getIdFrom() {
        return idFrom;
    }
    public void setIdFrom(int idFrom) {
        this.idFrom = idFrom;
    }
    public int getIdTo() {
        return idTo;
    }
    public void setIdTo(int idTo) {
        this.idTo = idTo;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getTypeContract() {
        return typeContract;
    }
    public void setTypeContract(String typeContract) {
        this.typeContract = typeContract;
    }

    

}
