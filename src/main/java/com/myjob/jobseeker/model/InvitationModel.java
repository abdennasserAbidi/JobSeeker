package com.myjob.jobseeker.model;

public class InvitationModel {

    private int idInvitation;
    private String message;
    private String description;
    private String typeContract;
    private int idTo;
    private int idCompany;
    private String companyName;
    private boolean accepted = false;

    public int getIdCompany() {
        return idCompany;
    }
    public void setIdCompany(int idCompany) {
        this.idCompany = idCompany;
    }
    
    public String getCompanyName() {
        return companyName;
    }
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getIdTo() {
        return idTo;
    }
    public void setIdTo(int idTo) {
        this.idTo = idTo;
    }

    
    public int getIdInvitation() {
        return idInvitation;
    }
    public void setIdInvitation(int idInvitation) {
        this.idInvitation = idInvitation;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
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
    public boolean isAccepted() {
        return accepted;
    }
    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    

}
