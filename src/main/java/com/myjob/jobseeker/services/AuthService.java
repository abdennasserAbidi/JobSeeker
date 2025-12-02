package com.myjob.jobseeker.services;

import com.myjob.jobseeker.dtos.*;
import com.myjob.jobseeker.interfaces.IAuthService;
import com.myjob.jobseeker.model.*;
import com.myjob.jobseeker.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class AuthService implements IAuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final PasswordResetService passwordResetService;

    @Autowired
    public AuthService(UserRepository userRepository,
                       AuthenticationManager authenticationManager,
                       PasswordEncoder passwordEncoder,
                       PasswordResetService passwordResetService) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.passwordResetService = passwordResetService;
    }

    @Override
    public void verifyAccountCompany(int id) {
        Optional<User> user = userRepository.findById(id);
        user.ifPresent(u -> {
            u.setVerified(true);
            userRepository.save(u);
        });
    }

    @Override
    public String updateToken(int email, String token) {
        Optional<User> user = userRepository.findById(email);
        user.ifPresent(u -> {
            u.setFcmToken(token);
            userRepository.save(u);
        });
        return "";
    }

    @Override
    public UserResponse signup(RegisterUserDto input) {
        User user = new User();
        user.setId(input.getId());
        user.setEmail(input.getEmail());
        user.setFullName(input.getFullName());
        user.setCompanyName(input.getCompanyName());
        user.setRole(input.getRole());
        user.setCandidate(input.isCandidate());
        user.setCompany(input.isCompany());
        user.setPreferredWorkType(input.getPreferredWorkType());
        user.setWorkPreferences(input.getWorkPreferences());
        user.setPassword(passwordEncoder.encode(input.getPassword()));

        boolean existingUser = userRepository.findByEmail(input.getEmail()).isPresent();

        UserResponse userResponse = new UserResponse();

        if (existingUser) {
            userResponse.setMessage("User already exist");
        } else userResponse.setUser(userRepository.save(user));

        return userResponse;
    }

    @Override
    public User authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return userRepository.findByEmail(input.getEmail()).orElseThrow();
    }

    @Override
    public User authenticateByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow();
    }

    // For password reset we delegate to PasswordResetService if already implemented
    @Override
    public String createPasswordResetTokenForUser(String userEmail) {
        return passwordResetService.createPasswordResetTokenForUser(userEmail);
    }

    @Override
    public boolean validatePasswordResetToken(String token) {
        return passwordResetService.validatePasswordResetToken(token);
    }

    @Override
    public void updatePassword(String token, String newPassword) {
        passwordResetService.updatePassword(token, newPassword);
    }

    @Override
    public void saveCandidateStatus(ValidationStatus validationStatus) {
        User user = userRepository.findById(validationStatus.getId()).orElseThrow();
        user.setValidationStatus(validationStatus);
        user.getValidationStepStatus().add(validationStatus);
        userRepository.save(user);
    }

    @Override
    public ValidationStatus getStatusCandidateValidation(int id) {
        User user = userRepository.findById(id).orElseThrow();
        ValidationStatus validationStatus;
        if (user.getValidationStatus() == null) validationStatus = new ValidationStatus();
        else validationStatus = user.getValidationStatus();

        return validationStatus;
    }

    @Override
    public java.util.List<ValidationStatus> getListStatusCandidateValidation(int id) {
        User user = userRepository.findById(id).orElseThrow();
        return user.getValidationStepStatus();
    }
    @Override
    public Page<User> getByCriteria(Criteria criteria, int page, int size) {
        List<User> users = userRepository.searchUsers(criteria);
        System.out.println("elkhlakgekage   users  "+users.size());
        List<User> newUser = new ArrayList<>();
        for (User user : users) {
            if (user.getRole().equals("Candidate") || user.getRole().equals("Candidat")) {
                newUser.add(user);
            }
        }
        System.out.println("elkhlakgekage   newUser  "+newUser.size());

        PageRequest pageable = PageRequest.of(page - 1, 3);
        final int start = (int) pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), newUser.size());

        Page<User> pager;

        if (start < newUser.size() && start < end) {
            pager = new PageImpl<>(newUser.subList(start, end), pageable, newUser.size());
        } else pager = new PageImpl<>(Collections.emptyList(), pageable, newUser.size());

        return pager;
    }
    @Override
    public Page<User> getNewCandidate(int id, int page, int size) {
        User user = userRepository.findById(id).orElseThrow();

        List<User> newUsers = new ArrayList<>();
        List<Integer> ids = new ArrayList<>();
        List<User> allUsers = userRepository.findAll();

        if (!user.getInvitations().isEmpty()) {
            for (InvitationModel i : user.getInvitations()) {
                ids.add(i.getIdTo());
            }
        }

        for (User candidate : allUsers) {
            if (!ids.contains(candidate.getId()) && candidate.isCandidate()) {
                newUsers.add(candidate);
            }
        }

        int s = Math.min(size, newUsers.size());

        PageRequest pageable = PageRequest.of(page - 1, s);
        final int start = (int) pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), s);
        return new PageImpl<>(newUsers.subList(start, end), pageable, s);
    }
    @Override
    public Page<User> getUsers(int id, int page, int size) {

        User user = userRepository.findById(id).orElseThrow();

        List<User> newUsers = new ArrayList<>();
        List<Integer> ids = new ArrayList<>();
        List<User> candidates = userRepository.findByRole("Candidat");
        List<User> allUsers = userRepository.findAll();

        System.out.println("jgrklznhkrnzh  " + candidates);

        for (User candidat : allUsers) {
            if (candidat.isCandidate()) {
                newUsers.add(candidat);
            }
        }

        /*if (!user.getInvitations().isEmpty()) {
            for (InvitationModel i : user.getInvitations()) {
                ids.add(i.getIdTo());
            }
        }


        for (User candidat : candidates) {
            if (!ids.contains(candidat.getId())) {
                newUsers.add(candidat);
            }
        }*/

        int s = Math.min(size, newUsers.size());

        PageRequest pageable = PageRequest.of(page - 1, s);
        final int start = (int) pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), s);
        return new PageImpl<>(newUsers.subList(start, end), pageable, s);
    }
    @Override
    public User getUser(int id) {
        System.out.println("fjkzbgjkbgzr  " + id);

        return userRepository.findById(id).orElseThrow();
    }
    @Override
    public void saveCompanyInfo(CompanyInfoDto input) {
        User user = userRepository.findById(input.getId()).orElseThrow();

        user.setCompanyName(input.getCompanyName());
        user.setCompanyActivitySector(input.getCompanyActivitySector());
        user.setCompanyDescription(input.getCompanyDescription());
        user.setLinkLinkedIn(input.getLinkLinkedIn());
        user.setFaxCompany(input.getFaxCompany());
        user.setPhoneCompany(input.getPhoneCompany());
        user.setCompanyAddress(input.getCompanyAddress());
        user.setSecondPhoneCompany(input.getSecondPhoneCompany());
        user.setCompanySecondAddress(input.getCompanySecondAddress());

        userRepository.save(user);
    }

    @Override
    public void completeUpdated(int id) {
        User user = userRepository.findById(id).orElseThrow();
        user.setFirstTime(false);
        user.setFirstTimeUse(false);
        userRepository.save(user);
    }
    @Override
    public void saveSkillsInfo(CandidateSkills candidateSkills) {
        User user = userRepository.findById(candidateSkills.getId()).orElseThrow();
        user.setCandidateSkills(candidateSkills);
        userRepository.save(user);
    }
    @Override
    public void saveProfessionalInfo(ProfessionalStatus professionalStatus) {
        User user = userRepository.findById(professionalStatus.getId()).orElseThrow();
        ProfessionalStatus.AvailabilityLabel availabilityLabel = new ProfessionalStatus.AvailabilityLabel();
        if (professionalStatus.getLanguage().equals("en")) {
            String toFr = switch (professionalStatus.getAvailability()) {
                case "Entry Level (0–1 years)" -> "Débutant (0–1 an)";
                case "Junior (1–3 years)" -> "Junior (1–3 ans)";
                case "Mid–Level (3–5 years)" -> "Intermédiaire (3–5 ans)";
                case "Senior (5–8 years)" -> "Sénior (5–8 ans)";
                case "Lead/Principal (8+ years)" -> "Chef d’équipe / Principal (8+ ans)";
                default -> "Cadre / Direction (10+ ans)";
            };
            availabilityLabel.setFr(toFr);
        } else {
            String toEn = switch (professionalStatus.getAvailability()) {
                case "Débutant (0–1 an)" -> "Entry Level (0–1 years)";
                case "Junior (1–3 ans)" -> "Junior (1–3 years)";
                case "Intermédiaire (3–5 ans)" -> "Mid–Level (3–5 years)";
                case "Sénior (5–8 ans)" -> "Senior (5–8 years)";
                case "Chef d’équipe / Principal (8+ ans)" -> "Lead/Principal (8+ years)";
                default -> "Executive (10+ years)";
            };
            availabilityLabel.setEn(toEn);
        }

        professionalStatus.setAvailabilityLabel(availabilityLabel);

        user.setProfessionalStatus(professionalStatus);
        userRepository.save(user);
    }

    @Override
    public void savePersonal(PersonalInfoDto input) {
        User user = userRepository.findById(input.getId()).orElseThrow();

        user.setFullName(input.getFullName());
        user.setBio(input.getBio());
        user.setNationality(input.getNationality());
        user.setActivitySector(input.getActivitySector());
        user.setAddress(input.getAddress());
        user.setCountry(input.getCountry());
        user.setBirthDate(input.getBirthDate());

        if (input.getSexe().equals("Homme") || input.getSexe().equals("Male")) user.setSexe("Male");
        else user.setSexe("Female");

        switch (input.getSituation()) {
            case "Single", "Célibataire" -> user.setSituation("Single");
            case "Engaged", "Engagé" -> user.setSituation("Engaged");
            case "Married", "Marrié" -> user.setSituation("Married");
        }

        switch (input.getPreferredEmploymentType()) {
            case "Contract", "Contrat" -> user.setPreferredEmploymentType("Contract");
            case "Freelance" -> user.setPreferredEmploymentType("Freelance");
            case "Both", "Les deux" -> user.setPreferredEmploymentType("Both");
        }

        /*switch (input.getAvailability()) {
            case "Now", "Maintenant" -> user.setAvailability("Now");
            case "Less than 3 months", "Avant 3 mois" -> user.setAvailability("Less than 3 months");
            case "In 3 months", "Dans 3 mois" -> user.setAvailability("In 3 months");
            case "More than 3 months", "Après 3 mois" -> user.setAvailability("More than 3 months");
        }*/

        user.setRangeSalary(input.getRangeSalary());
        user.setPreferredActivitySector(input.getPreferredActivitySector());
        user.setPhone(input.getPhone());

        userRepository.save(user);
    }

    @Override
    public Page<User> getUsersFavorites(int id, int page, int size) {

        User user = userRepository.findById(id).orElseThrow();

        List<User> newUsers = new ArrayList<>();

        if (!user.getFavorites().isEmpty()) {
            for (FavoriteModel i : user.getFavorites()) {
                User favoriteUser = userRepository.findById(i.getId()).orElseThrow();
                newUsers.add(favoriteUser);
            }
        }

        PageRequest pageable = PageRequest.of(page - 1, 3);
        final int start = (int) pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), newUsers.size());

        return new PageImpl<>(newUsers.subList(start, end), pageable, newUsers.size());

    }

}