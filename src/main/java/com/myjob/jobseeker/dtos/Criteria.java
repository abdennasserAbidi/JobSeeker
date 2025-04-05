package com.myjob.jobseeker.dtos;

import java.util.ArrayList;
import java.util.List;

public class Criteria {

    private int id;
    private String experiences = "";
    private String location = "";
    private List<String> typeContract = new ArrayList<>();
    private List<String> institutions = new ArrayList<>();
    private List<String> activitySectors = new ArrayList<>();
    private List<String> companies = new ArrayList<>();

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getExperiences() {
        return experiences;
    }
    public void setExperiences(String experiences) {
        this.experiences = experiences;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public List<String> getTypeContract() {
        return typeContract;
    }
    public void setTypeContract(List<String> typeContract) {
        this.typeContract = typeContract;
    }
    public List<String> getInstitutions() {
        return institutions;
    }
    public void setInstitutions(List<String> institutions) {
        this.institutions = institutions;
    }
    public List<String> getActivitySectors() {
        return activitySectors;
    }
    public void setActivitySectors(List<String> activitySectors) {
        this.activitySectors = activitySectors;
    }
    public List<String> getCompanies() {
        return companies;
    }
    public void setCompanies(List<String> companies) {
        this.companies = companies;
    }
}
