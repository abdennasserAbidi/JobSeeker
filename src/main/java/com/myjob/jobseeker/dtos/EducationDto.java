package com.myjob.jobseeker.dtos;

import lombok.Data;

@Data
public class EducationDto {

    private int id;
    private int idUser;
    private String message;
    private String title;
    private String schoolName;
    private String dateStart;
    private String dateEnd;
    private String place;
    private String fieldStudy;
    private String grade;
    private String description;
    private String degree;
    private boolean stillStudying;
}
