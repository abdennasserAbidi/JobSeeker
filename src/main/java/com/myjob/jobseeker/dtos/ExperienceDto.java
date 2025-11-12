package com.myjob.jobseeker.dtos;

import lombok.Data;

import java.util.List;

@Data
public class ExperienceDto {

    private int id;
    private int idUser;
    private String message;
    private String title;
    private String companyName;
    private String dateStart;
    private boolean isFreelance;
    private boolean isContract;
    private boolean perHourPaymentMethod;
    private boolean perDayPaymentMethod;
    private boolean perProjectPaymentMethod;
    private boolean current;
    private String dateEnd;
    private String place;
    private String type;
    private String typeContract;
    private int salary;
    private int freelanceSalary;
    private String freelanceFee;
    private String anotherActivitySector;
    private int nbHours;
    private int nbDays;
    private int hourlyRate;
    private List<String> listSkills;
}
