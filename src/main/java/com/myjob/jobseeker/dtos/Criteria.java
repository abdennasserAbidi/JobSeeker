package com.myjob.jobseeker.dtos;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Criteria {
    private int id;
    private int idUser;
    private List<String> situation = new ArrayList<>();
    private List<String> status = new ArrayList<>();
    private List<String> disponibility = new ArrayList<>();
    private List<String> sex = new ArrayList<>();
    private List<String> experiences = new ArrayList<>();
    private List<String> categories = new ArrayList<>();
    private List<String> location = new ArrayList<>();
    private List<String> typeContract = new ArrayList<>();
    private List<String> institutions = new ArrayList<>();
    private List<String> preferredActivitySector = new ArrayList<>();
    private List<String> companies = new ArrayList<>();


    @Override
    public String toString() {
        return "Criteria{" +
                "situation=" + situation +
                ", status=" + status +
                ", disponibility=" + disponibility +
                ", sex=" + sex +
                ", experiences=" + experiences +
                ", categories=" + categories +
                ", location=" + location +
                ", typeContract=" + typeContract +
                ", institutions=" + institutions +
                ", preferredActivitySector=" + preferredActivitySector +
                ", companies=" + companies +
                ", id=" + id +
                '}';
    }
}
