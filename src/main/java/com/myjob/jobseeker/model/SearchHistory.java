package com.myjob.jobseeker.model;

import lombok.Data;

@Data
public class SearchHistory {

    private int id;
    private int idUser;
    private String gender;
    private String fullName;
    private String experience;

}
