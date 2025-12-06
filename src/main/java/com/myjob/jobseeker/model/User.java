package com.myjob.jobseeker.model;

import com.myjob.jobseeker.model.chat.ChatModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "User")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ValidationStatus> validationStepStatus = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Experience> experiences = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Education> education = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FavoriteModel> favorites = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvitationModel> invitations = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SearchHistory> searchHistories = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AnnounceModel> announces = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NotificationModel> notifications = new ArrayList<>();

    private List<ChatModel> messages = new ArrayList<>();

    private List<String> preferredWorkType = new ArrayList<>();
    private List<String> workPreferences = new ArrayList<>();

    public void setIsResetPasswordTokenValid(boolean isResetPasswordTokenValid) {
        this.isResetPasswordTokenValid = isResetPasswordTokenValid;
    }

    public boolean getIResetPasswordTokenValid() {
        return isResetPasswordTokenValid;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return email;
    }

}
