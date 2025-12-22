package com.myjob.jobseeker.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Document(collection = "notifications")
public class NotificationModel {

    private int idNotification;
    private String title;
    private String description;
    private String date;
    private int idCompany;
    private String companyName;
    private int idCandidate;
    private String username;
    private boolean isRead = false;
}
