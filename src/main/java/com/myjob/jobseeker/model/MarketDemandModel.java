package com.myjob.jobseeker.model;

import lombok.Data;
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
    private int idCompany;
    private String companyName;
    private int idCandidate;
    private String username;
}