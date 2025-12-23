package com.myjob.jobseeker.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "notifications")
public class NotificationModel {

    @Id
    private int idNotification;
    private String title;
    private String description;
    private String date;
    private int idSender;
    private int idCompany;
    private String companyName;
    private int idCandidate;
    private String username;
    private boolean isRead;
}
