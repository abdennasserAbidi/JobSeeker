package com.myjob.jobseeker.services;

import com.myjob.jobseeker.dtos.*;
import com.myjob.jobseeker.interfaces.IUserService;
import com.myjob.jobseeker.model.*;
import com.myjob.jobseeker.repo.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Collections;
import java.util.regex.Pattern;

@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
    public void saveSkillsInfo(CandidateSkills candidateSkills) {
        User user = userRepository.findById(candidateSkills.getId()).orElseThrow();
        user.setCandidateSkills(candidateSkills);
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

        user.setRangeSalary(input.getRangeSalary());
        user.setPreferredActivitySector(input.getPreferredActivitySector());
        user.setPhone(input.getPhone());

        userRepository.save(user);
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
    public User getUser(int id) {
        return userRepository.findById(id).orElseThrow();
    }

    @Override
    public List<User> getCandidate() {
        List<User> list = userRepository.findAll();
        return list.stream().filter(user -> user.getRole().equals("Candidate") || user.getRole().equals("Candidat")).toList();
    }

    @Override
    public Page<User> getUsers(int id, int page, int size) {
        User user = userRepository.findById(id).orElseThrow();

        List<User> newUsers = new ArrayList<>();
        List<User> allUsers = userRepository.findAll();

        for (User candidat : allUsers) {
            if (candidat.isCandidate()) {
                newUsers.add(candidat);
            }
        }

        int s = Math.min(size, newUsers.size());

        PageRequest pageable = PageRequest.of(page - 1, s);
        final int start = (int) pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), s);
        return new PageImpl<>(newUsers.subList(start, end), pageable, s);
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

    @Override
    public Page<User> getByCriteria(Criteria criteria, int page, int size) {
        List<User> users = userRepository.searchUsers(criteria);
        List<User> newUser = new ArrayList<>();
        for (User user : users) {
            if (user.getRole().equals("Candidate") || user.getRole().equals("Candidat")) {
                newUser.add(user);
            }
        }

        PageRequest pageable = PageRequest.of(page - 1, 3);
        final int start = (int) pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), newUser.size());

        Page<User> pager;

        if (start < newUser.size() && start < end) {
            pager = new PageImpl<>(newUser.subList(start, end), pageable, newUser.size());
        } else pager = new PageImpl<>(Collections.emptyList(), pageable, newUser.size());

        return pager;
    }
}