package com.myjob.jobseeker.dtos;

public class CompanyInfoDto {

    private int id;
    private String companyName;
    private String phoneCompany;
    private String faxCompany;
    private String linkLinkedIn;
    private String companyActivitySector;
    private String companyDescription;
    private String companyAddress;
    private String companySecondAddress;
    private String secondPhoneCompany;

    public int getCompanySecondAddress() {
        return companySecondAddress;
    }

    public void setCompanySecondAddress(String companySecondAddress) {
        this.companySecondAddress = companySecondAddress;
    }

    public int getSecondPhoneCompany() {
        return secondPhoneCompany;
    }

    public void setSecondPhoneCompany(String secondPhoneCompany) {
        this.secondPhoneCompany = secondPhoneCompany;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPhoneCompany() {
        return phoneCompany;
    }

    public void setPhoneCompany(String phoneCompany) {
        this.phoneCompany = phoneCompany;
    }

    public String getFaxCompany() {
        return faxCompany;
    }

    public void setFaxCompany(String faxCompany) {
        this.faxCompany = faxCompany;
    }

    public String getLinkLinkedIn() {
        return linkLinkedIn;
    }

    public void setLinkLinkedIn(String linkLinkedIn) {
        this.linkLinkedIn = linkLinkedIn;
    }

    public String getCompanyActivitySector() {
        return companyActivitySector;
    }

    public void setCompanyActivitySector(String companyActivitySector) {
        this.companyActivitySector = companyActivitySector;
    }

    public String getCompanyDescription() {
        return companyDescription;
    }

    public void setCompanyDescription(String companyDescription) {
        this.companyDescription = companyDescription;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    

}
