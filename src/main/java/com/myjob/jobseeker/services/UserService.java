package com.myjob.jobseeker.services;

import com.myjob.jobseeker.dtos.*;
import com.myjob.jobseeker.interfaces.IUserService;
import com.myjob.jobseeker.model.*;
import com.myjob.jobseeker.repo.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;
import java.util.Collections;

@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll().stream().filter(user -> user.isFirstTimeUse() == false).toList();
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
        System.out.println("lgrjelgkejgrjeglr    "+ input);
        User user = userRepository.findById(input.getId()).orElseThrow();

        user.setFullName(input.getFullName());
        user.setBio(input.getBio());
        user.setNationality(input.getNationality());
        user.setActivitySector(input.getActivitySector());
        user.setAddressList(input.getAddressList());
        user.setCountry(input.getCountry());
        user.setBirthDate(input.getBirthDate());
        user.setPreferredWorkType(input.getPreferredWorkType());

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
        user.setPhoneList(input.getPhoneList());

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
    public void saveServiceInfo(ServiceInfoDto input) {
        User user = userRepository.findById(input.getId()).orElseThrow();

        user.setAddressList(input.getAddressList());
        user.setPhoneList(input.getPhoneList());
        user.setCategory(input.getCategory());
        user.setCountry(input.getCountry());
        user.setCity(input.getCity());
        user.setBio(input.getBio());
        user.setEmail(input.getEmail());
        user.setOtherCategory(input.getOtherCategory());

        userRepository.save(user);
    }

    @Override
    public UserResponse getUser(int id) {

        Optional<User> user = userRepository.findById(id);
        User user1 = user.orElseGet(User::new);
        UserResponse userResponse = new UserResponse();
        userResponse.setUser(user1);
        if (user1.getId() == 0) userResponse.setMessage("There are no such user");
        else userResponse.setMessage("");

        return userResponse;
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
    public Page<User> getUserServiceFiltered(String word, int page, int size) {

        List<User> newUsers = new ArrayList<>();

        AtomicReference<Page<User>> userPage = new AtomicReference<>();
        userPage.set(new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10), 0));


        List<User> allUser = userRepository.findAll();

        for (User user : allUser) {

                boolean nameContains = Pattern.compile(Pattern.quote(word), Pattern.CASE_INSENSITIVE).matcher(user.getUsername()).find();
                boolean descriptionContains = Pattern.compile(Pattern.quote(word), Pattern.CASE_INSENSITIVE).matcher(user.getBio()).find();

                boolean categoryContains = Pattern.compile(Pattern.quote(word), Pattern.CASE_INSENSITIVE).matcher(user.getCategory().getDisplayName()).find();
                boolean otherategoryContains = Pattern.compile(Pattern.quote(word), Pattern.CASE_INSENSITIVE).matcher(user.getOtherCategory()).find();

                if (nameContains || descriptionContains || categoryContains || otherategoryContains) {
                    newUsers.add(user);
                }
        }

        System.out.println("felkaefjajfeiahe  word  "+word);
        System.out.println("felkaefjajfeiahe    "+newUsers);

        if (newUsers.isEmpty()) {
                userPage.set(new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10), 0));
        } else {
                int s = Math.min(size, newUsers.size());

                PageRequest pageable = PageRequest.of(page - 1, s);
                final int start = (int) pageable.getOffset();
                final int end = Math.min((start + pageable.getPageSize()), s);
                userPage.set(new PageImpl<>(newUsers.subList(start, end), pageable, s));
        }

        return userPage.get();
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
            boolean isCandidate = user.getRole().equals("Candidate") || user.getRole().equals("Candidat");
            if (!user.isFirstTimeUse() && isCandidate) {
                newUser.add(user);
            }
        }

        PageRequest pageable = PageRequest.of(page - 1, size);
        final int start = (int) pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), newUser.size());

        Page<User> pager;

        if (start < newUser.size() && start < end) {
            pager = new PageImpl<>(newUser.subList(start, end), pageable, newUser.size());
        } else pager = new PageImpl<>(Collections.emptyList(), pageable, newUser.size());

        return pager;
    }
}