package com.myjob.jobseeker.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SearchHistory {

    private int id;
    private int idUser;
    private String gender;
    private String fullName;
    private String experience;

}
