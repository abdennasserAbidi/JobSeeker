package com.myjob.jobseeker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Education")
public class Education {
    @Id
    private int id;

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
    private User user;
}
