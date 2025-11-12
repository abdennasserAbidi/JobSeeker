package com.myjob.jobseeker.model;

import lombok.Data;

import java.util.List;

@Data
public class CandidateSkills {
    private int id;
    private List<String> listSkills;
    private List<String> listCertification;
    private List<LanguageForm> listLanguages;
}
