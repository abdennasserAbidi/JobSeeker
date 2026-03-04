package com.myjob.jobseeker.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

import com.myjob.jobseeker.model.Gender;
import com.myjob.jobseeker.model.Situation;

@Setter
@Getter
public class PersonalInfoDto {

    private int id;

    private String nationality;

    private String bio;

    private List<String> addressList;

    private Gender sexe;

    private String country;

    private String fullName;

    private String birthDate;

    private String activitySector;

    private Situation situation;

    private String rangeSalary;

    private String preferredActivitySector;

    private String preferredEmploymentType;

    private List<String> phoneList;

    private List<String> preferredWorkType;
}