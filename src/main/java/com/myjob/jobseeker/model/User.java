package com.myjob.jobseeker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "User")
public class User implements UserDetails {
    private int id;
    private String fullName;
    private String email;
    private String password;
    private String companyName;
    private String bio;

    private String fcmToken;

    private Date createdAt;

    private Date updatedAt;

    private boolean isResetPasswordTokenValid;

    private String nationality;

    private String address;

    private String role;
    private boolean candidate;
    private boolean company;

    private String sexe;
    
    private String birthDate;

    private String activitySector;

    private String situation;

    private String status = "Available";

    private String rangeSalary;

    private String preferredActivitySector;

    private String preferredEmploymentType;

    private String country;

    private String phone;

    private boolean firstTime;
    private boolean firstTimeUse = true;

    private boolean isFavorite;

    private boolean isVerified = false;


    private String phoneCompany;

    private String faxCompany;
    
    private String linkLinkedIn;
    
    private String companyActivitySector;
    
    private String companyDescription;

    private String companyAddress;

    private String companySecondAddress;
    
    private String secondPhoneCompany;

    private ValidationStatus validationStatus;
    private ProfessionalStatus professionalStatus;
    private CandidateSkills candidateSkills;

    private List<ValidationStatus> validationStepStatus = new ArrayList<>();

    private List<Experience> experiences = new ArrayList<>();

    private List<Education> education = new ArrayList<>();

    private List<FavoriteModel> favorites = new ArrayList<>();

    private List<InvitationModel> invitations = new ArrayList<>();

    private List<SearchHistory> searchHistories = new ArrayList<>();

    private List<AnnounceModel> announces = new ArrayList<>();

    private List<NotificationModel> notifications = new ArrayList<>();

    private List<String> preferredWorkType = new ArrayList<>();
    private List<String> workPreferences = new ArrayList<>();

    public void setIsResetPasswordTokenValid(boolean isResetPasswordTokenValid) {
        this.isResetPasswordTokenValid = isResetPasswordTokenValid;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
