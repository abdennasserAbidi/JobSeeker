package com.myjob.jobseeker.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ValidationStatus {
    int id;
    String email = "";
    String linkedin = "";
    String typeValidation = "";
    String registrationNumber;
    String status = "NOT_STARTED";
    List<String> docs = new ArrayList<>();
}
