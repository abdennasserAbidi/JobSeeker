package com.myjob.jobseeker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Experience")
public class Experience {

    @Id
    private int id;

    private String title;
    private String companyName;
    private String dateStart;
    private String dateEnd;
    private String place;
    private String type;
    private boolean isFreelance;
    private boolean isContract;
    private boolean perHourPaymentMethod;
    private boolean perDayPaymentMethod;
    private boolean perProjectPaymentMethod;
    private boolean current;
    private String typeContract;
    private int salary;
    private int freelanceSalary;
    private String freelanceFee;
    private String anotherActivitySector;
    private int nbHours;
    private int nbDays;
    private int hourlyRate;
    private List<String> listSkills;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
