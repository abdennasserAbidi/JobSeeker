package com.myjob.jobseeker.model;

import lombok.Data;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "demands")
public class MarketDemandModel {

    @Id
    private int id;
    private String title;
    private String description;
    private String date;
    private int idSender;
    private User userSender;
    private boolean paidUser;
    private int countTrial = 10;
    private ServiceCategory category;
    private String location;
    private String budget;
    private String urgency;
    private String status;
    private String deadline;
    private List<String> images;
}