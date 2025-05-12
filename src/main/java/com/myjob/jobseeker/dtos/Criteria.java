package com.myjob.jobseeker.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class Criteria {

    @Getter
    @Setter
    private int id;
    @Setter
    @Getter
    private List<String> situation = new ArrayList<>();
    @Setter
    @Getter
    private List<String> disponibility = new ArrayList<>();
    @Setter
    @Getter
    private List<String> sex = new ArrayList<>();
    @Setter
    @Getter
    private List<String> experiences = new ArrayList<>();
    @Setter
    @Getter
    private List<String> categories = new ArrayList<>();
    @Setter
    @Getter
    private List<String> location = new ArrayList<>();
    @Setter
    @Getter
    private List<String> typeContract = new ArrayList<>();
    @Setter
    @Getter
    private List<String> institutions = new ArrayList<>();
    @Setter
    @Getter
    private List<String> preferredActivitySector = new ArrayList<>();
    @Setter
    @Getter
    private List<String> companies = new ArrayList<>();


    @Override
    public String toString() {
        return "Criteria [id=" + id + ", experiences=" + experiences + ", location=" + location + ", typeContract="
                + typeContract + ", institutions=" + institutions + ", activitySectors=" + preferredActivitySector
                + ", companies=" + companies + "]";
    }
}
