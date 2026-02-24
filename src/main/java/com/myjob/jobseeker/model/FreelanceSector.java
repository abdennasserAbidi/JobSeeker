package com.myjob.jobseeker.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class FreelanceSector {
    private String id;
    private String name;
    private String icon;
    private String description;
    private List<FreelanceService> services = new ArrayList<>();

}
