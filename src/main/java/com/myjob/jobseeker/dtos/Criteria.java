package com.myjob.jobseeker.dtos;

import java.util.ArrayList;
import java.util.List;

public class Criteria {

    private int id;
    private List<String> situation = new ArrayList<>();
    private List<String> disponibility = new ArrayList<>();
    private List<String> sex = new ArrayList<>();
    private List<String> experiences = new ArrayList<>();
    private List<String> location = new ArrayList<>();
    private List<String> typeContract = new ArrayList<>();
    private List<String> institutions = new ArrayList<>();
    private List<String> preferredActivitySector = new ArrayList<>();
    private List<String> companies = new ArrayList<>();

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public List<String> getDisponibility() {
        return disponibility;
    }

    public void setDisponibility(List<String> disponibility) {
        this.disponibility = disponibility;
    }

    public List<String> getSituation() {
        return situation;
    }

    public void setSituation(List<String> situation) {
        this.situation = situation;
    }

    public List<String> getSex() {
        return sex;
    }

    public void setSex(List<String> sex) {
        this.sex = sex;
    }

    public List<String> getExperiences() {
        return experiences;
    }

    public void setExperiences(List<String> experiences) {
        this.experiences = experiences;
    }

    public List<String> getLocation() {
        return location;
    }

    public void setLocation(List<String> location) {
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
    
    public List<String> getCompanies() {
        return companies;
    }
    public void setCompanies(List<String> companies) {
        this.companies = companies;
    }

    @Override
    public String toString() {
        return "Criteria [id=" + id + ", experiences=" + experiences + ", location=" + location + ", typeContract="
                + typeContract + ", institutions=" + institutions + ", activitySectors=" + preferredActivitySector
                + ", companies=" + companies + "]";
    }
    public List<String> getPreferredActivitySector() {
        return preferredActivitySector;
    }
    public void setPreferredActivitySector(List<String> preferredActivitySector) {
        this.preferredActivitySector = preferredActivitySector;
    }

    
}
