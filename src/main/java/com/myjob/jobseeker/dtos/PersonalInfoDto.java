package com.myjob.jobseeker.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PersonalInfoDto {

    private int id;

    private String nationality;

    private String bio;

    private String address;

    private String sexe;

    private String country;

    private String fullName;

    private String birthDate;

    private String activitySector;

    private String situation;

    private String rangeSalary;

    private String preferredActivitySector;

    private String preferredEmploymentType;

    private String phone;
}
