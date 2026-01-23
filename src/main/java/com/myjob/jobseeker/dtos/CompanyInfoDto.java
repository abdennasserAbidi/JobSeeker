package com.myjob.jobseeker.dtos;

import lombok.Data;

@Data
public class CompanyInfoDto {
    private int id;
    private String companyName;
    private String phoneCompany;
    private String country;
    private String faxCompany;
    private String linkLinkedIn;
    private String companyActivitySector;
    private String companyDescription;
    private String companyAddress;
    private String companySecondAddress;
    private String secondPhoneCompany;
}