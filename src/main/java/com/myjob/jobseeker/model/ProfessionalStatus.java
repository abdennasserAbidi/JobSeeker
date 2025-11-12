package com.myjob.jobseeker.model;

import lombok.Data;

@Data
public class ProfessionalStatus {

    private int id;
    private String userExperience;
    private String availability;
    private String workType;
    private AvailabilityLabel availabilityLabel;
    private String preferredSalary;
    private String userGithub;
    private String userMedium;
    private String userPortfolio;
    private String language;
    private boolean onSitePreference;
    private boolean hybridPreference;
    private boolean remotePreference;

    @Data
    public static class AvailabilityLabel {
        private String en;
        private String fr;
    }
}
