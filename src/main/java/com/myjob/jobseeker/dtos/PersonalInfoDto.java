package com.myjob.jobseeker.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class PersonalInfoDto {

    private int id;

    private String nationality;

    private String bio;

    private List<String> addressList;

    private String sexe;

    private String country;

    private String fullName;

    private String birthDate;

    private String activitySector;

    private String situation;

    private String rangeSalary;

    private String preferredActivitySector;

    private String preferredEmploymentType;

    private List<String> phoneList;

    private List<String> preferredWorkType;
}
