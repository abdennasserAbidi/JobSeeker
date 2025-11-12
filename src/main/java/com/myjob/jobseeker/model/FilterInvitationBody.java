package com.myjob.jobseeker.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FilterInvitationBody {
    private int id;
    private int idUser;
    private String type;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<String> listTypeContract = new ArrayList<>();
}
