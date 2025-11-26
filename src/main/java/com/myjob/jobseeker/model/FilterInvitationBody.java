package com.myjob.jobseeker.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FilterInvitationBody {
    private int id;
    private int idUser;
    private String type;
    private List<String> listTypeContract = new ArrayList<>();
}
