package com.myjob.jobseeker.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

import com.myjob.jobseeker.model.FreelanceSector;
import com.myjob.jobseeker.model.FreelanceService;
import com.myjob.jobseeker.model.ServiceCategory;

@Setter
@Getter
public class RegisterUserDto {

    private int id;

    private String email;

    private String password;

    private String fullName;

    private String companyName;

    private String role;

    private boolean candidate;
    private boolean company;

    private List<String> preferredWorkType;

    private List<String> workPreferences;

    private boolean service;
    private String userServiceName;
    private boolean paidUser;
    private int countTrial = 10;
    private ServiceCategory category;
    private FreelanceService freelanceService;
    private FreelanceSector freelanceSector;
    private String otherCategory;


}
