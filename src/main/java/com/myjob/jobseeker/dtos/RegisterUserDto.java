package com.myjob.jobseeker.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

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


}
