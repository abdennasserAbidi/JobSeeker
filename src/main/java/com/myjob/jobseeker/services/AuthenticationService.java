package com.myjob.jobseeker.services;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.myjob.jobseeker.dtos.*;
import com.myjob.jobseeker.model.*;
import com.myjob.jobseeker.model.post.CommentsPost;
import com.myjob.jobseeker.model.post.LikesPost;
import com.myjob.jobseeker.repo.StoredFileRepository;
import com.myjob.jobseeker.repo.UserRepository;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    private StoredFileRepository repository;

    @Autowired
    FirebaseMessaging firebaseMessaging;

    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void verifyAccountCompany(int id) {
        Optional<User> user = userRepository.findById(id);
        user.ifPresent(u -> {
            u.setVerified(true);
            userRepository.save(u);
        });
    }

    public String updateToken(int email, String token) {
        Optional<User> user = userRepository.findById(email);
        user.ifPresent(u -> {
            u.setFcmToken(token);
            userRepository.save(u);
        });
        return "";
    }

    public String sendNotification(NotificationMessage notificationMessage) {

        Notification notification = Notification.builder()
                .setTitle(notificationMessage.getTitle())
                .setBody(notificationMessage.getBody())
                .build();

        Message message = Message.builder()
                .setToken(notificationMessage.getRecipientToken())
                //.setNotification(notification)
                .putAllData(notificationMessage.getData())
                .build();

        try {
            firebaseMessaging.send(message);
            return "Success sending notification";
        } catch (Exception e) {
            return "Error sending notification";
        }

    }

    public void updateFavorite(int idConnected, int id) {
        User user = userRepository.findById(idConnected).orElseThrow();

        List<FavoriteModel> l = user.getFavorites();
        FavoriteModel f = new FavoriteModel();

        User candidate = userRepository.findById(id).orElseThrow();
        candidate.setFavorite(true);

        f.setId(candidate.getId());
        f.setEmail(candidate.getEmail());
        f.setName(candidate.getFullName());
        f.setPhone(candidate.getPhone());

        l.add(f);
        user.setFavorites(l);

        userRepository.save(user);
    }


    public Page<FavoriteModel> getPaginatedFavorites(int id, int page, int size) {
        return userRepository.findPaginatedFavorites(id, page, size);
    }

    public Page<User> getUserFiltered(String word,
                                      int id, int page, int size) {

        User user = userRepository.findById(id).orElseThrow();

        List<User> newUsers = new ArrayList<>();
        List<Integer> ids = new ArrayList<>();
        List<User> candidates = userRepository.findByRole("Candidate");

        if (!user.getInvitations().isEmpty()) {
            for (InvitationModel i : user.getInvitations()) {
                ids.add(i.getIdTo());
            }
        }

        for (User candidat : candidates) {
            boolean nameContains = Pattern.compile(Pattern.quote(word), Pattern.CASE_INSENSITIVE).matcher(candidat.getFullName()).find();

            if (nameContains) {
                newUsers.add(candidat);
            }
        }

        if (newUsers.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10), 0);

        } else {
            int s = Math.min(size, newUsers.size());

            PageRequest pageable = PageRequest.of(page - 1, s);
            final int start = (int) pageable.getOffset();
            final int end = Math.min((start + pageable.getPageSize()), s);
            return new PageImpl<>(newUsers.subList(start, end), pageable, s);
        }
    }

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

    public void saveSkillsInfo(CandidateSkills candidateSkills) {
        User user = userRepository.findById(candidateSkills.getId()).orElseThrow();
        user.setCandidateSkills(candidateSkills);
        userRepository.save(user);
    }

    public void completeUpdated(int id) {
        User user = userRepository.findById(id).orElseThrow();
        user.setFirstTime(false);
        user.setFirstTimeUse(false);
        userRepository.save(user);
    }

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

    public User getUser(int id) {
        System.out.println("fjkzbgjkbgzr  " + id);

        return userRepository.findById(id).orElseThrow();
    }

    public void saveEducation(EducationDto input) {


        boolean isPresent = false;
        int index = -1;

        Education experience = new Education();
        experience.setId(input.getId());
        experience.setTitle(input.getTitle());
        experience.setSchoolName(input.getSchoolName());
        experience.setDateStart(input.getDateStart());
        experience.setDateEnd(input.getDateEnd());
        experience.setFieldStudy(input.getFieldStudy());
        experience.setPlace(input.getPlace());
        experience.setDegree(input.getDegree());
        experience.setDescription(input.getDescription());
        experience.setGrade(input.getGrade());
        experience.setStillStudying(input.isStillStudying());

        User user = userRepository.findById(input.getIdUser()).orElseThrow();
        List<Education> list = user.getEducation();

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == input.getId()) {
                isPresent = true;
                index = i;
            }
        }

        if (isPresent) {
            list.set(index, experience);
        } else {
            list.add(experience);
            user.setEducation(list);
        }
        userRepository.save(user);
    }

    public List<Education> getAllEducations(int id) {
        User user = userRepository.findById(id).orElseThrow();
        return user.getEducation();
    }

    public void saveExperience(ExperienceDto input) {

        boolean isPresent = false;
        int index = -1;

        Experience experience = new Experience();
        experience.setId(input.getId());
        experience.setTitle(input.getTitle());
        experience.setType(input.getType());
        experience.setTypeContract(input.getTypeContract());
        experience.setDateStart(input.getDateStart());
        experience.setDateEnd(input.getDateEnd());
        experience.setCompanyName(input.getCompanyName());
        experience.setFreelance(input.isFreelance());
        experience.setContract(input.isContract());
        experience.setCompanyName(input.getCompanyName());
        experience.setPlace(input.getPlace());
        experience.setSalary(input.getSalary());
        experience.setFreelanceFee(input.getFreelanceFee());
        experience.setAnotherActivitySector(input.getAnotherActivitySector());
        experience.setHourlyRate(input.getHourlyRate());
        experience.setNbDays(input.getNbDays());
        experience.setNbHours(input.getNbHours());
        experience.setFreelanceSalary(input.getFreelanceSalary());
        experience.setPerHourPaymentMethod(input.isPerHourPaymentMethod());
        experience.setPerDayPaymentMethod(input.isPerDayPaymentMethod());
        experience.setPerProjectPaymentMethod(input.isPerProjectPaymentMethod());
        experience.setCurrent(input.isCurrent());
        experience.setListSkills(input.getListSkills());

        User user = userRepository.findById(input.getIdUser()).orElseThrow();
        List<Experience> list = user.getExperiences();

        String userExperience = "";
        int size = list.size();

        if (size <= 2) {
            userExperience = "Premier emploi";
        } else if (size >= 2 && size <= 5) {
            userExperience = "Confirmé";
        } else if (size >= 6 && size <= 8) {
            userExperience = "Cadre";
        } else if (size >= 8 && size <= 10) {
            userExperience = "Directeur";
        } else if (size > 10) {
            userExperience = "Cadre supérieur";
        } else {
            userExperience = "Inconnu";
        }

        //user.setUserExperience(userExperience);

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == input.getId()) {
                isPresent = true;
                index = i;
            }
        }

        if (isPresent) {
            list.set(index, experience);
        } else {
            list.add(experience);
            user.setExperiences(list);
        }
        userRepository.save(user);
    }

    public void saveUserInvitation(int idUser, InvitationModel input) {

        boolean isPresent = false;
        int index = -1;

        User user = userRepository.findById(idUser).orElseThrow();
        List<InvitationModel> list = user.getInvitations();

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getIdInvitation() == input.getIdInvitation()) {
                isPresent = true;
                index = i;
            }
        }

        if (isPresent) {
            list.set(index, input);
        } else {
            list.add(input);
            user.setInvitations(list);
        }

        userRepository.save(user);
    }

    public void saveSearchHistory(int idUserConnected, SearchHistory searchHistory) {

        boolean isPresent = false;

        User user = userRepository.findById(idUserConnected).orElseThrow();
        List<SearchHistory> list = user.getSearchHistories();

        for (SearchHistory search : list) {
            if (search.getIdUser() == searchHistory.getIdUser()) {
                isPresent = true;
                break;
            }
        }

        if (!isPresent) {
            list.add(searchHistory);
            System.out.println(list);
            user.setSearchHistories(list);
        }

        userRepository.save(user);
    }

    public Page<SearchHistory> getPaginatedSearches(int id, int page, int size) {
        return userRepository.findPaginatedSearches(id, page, size);
    }

    public void saveCompanyInvitation(int id, InvitationModel input) {

        User user = userRepository.findById(id).orElseThrow();
        List<InvitationModel> list = user.getInvitations();

        list.add(input);
        user.setInvitations(list);

        userRepository.save(user);
    }

    public void updateStatus(int idCandidate, InvitationModel input) {
        User candidate = userRepository.findById(idCandidate).orElseThrow();
        List<InvitationModel> list = candidate.getInvitations();

        InvitationModel i = null;
        int index = -1;
        for (int j = 0; j < list.size(); j++) {
            InvitationModel invitationModel = list.get(j);
            if (invitationModel.getIdInvitation() == input.getIdInvitation()) {
                index = j;
                i = invitationModel;
            }
        }

        if (i != null) {
            i.setIdInvitation(input.getIdInvitation());
            i.setIdTo(input.getIdTo());
            i.setStatus(input.getStatus());
            i.setAccepted(input.isAccepted());
            i.setIdCompany(input.getIdCompany());
            i.setCompanyName(input.getCompanyName());
            i.setMessage(input.getMessage());
            i.setDescription(input.getDescription());
            i.setTypeContract(input.getTypeContract());
            i.setDate(input.getDate());
            i.setFullName(input.getFullName());
            i.setGender(input.getGender());

            list.set(index, i);
            candidate.setInvitations(list);

            userRepository.save(candidate);
        }
    }

    public String acceptRejectInvitation(InvitationDto invitationDto) {
        InvitationModel input = invitationDto.getInvitationModel();

        int idCandidate = input.getIdTo();
        updateStatus(idCandidate, input);

        int idCompany = invitationDto.getIdConnected();
        updateStatus(idCompany, input);

        return input.getStatus();
    }

    public void sendInvitation(int id, InvitationModel input) {

        User user = userRepository.findById(id).orElseThrow();
        List<InvitationModel> list = user.getInvitations();
        List<InvitationModel> filteredList = list.stream().filter(invitation -> invitation.getIdTo() == input.getIdTo()).toList();

        if (filteredList.isEmpty()) {
            //company
            InvitationModel experience = new InvitationModel();
            experience.setIdInvitation(input.getIdInvitation());
            experience.setIdTo(input.getIdTo());
            experience.setIdCompany(input.getIdCompany());
            experience.setCompanyName(input.getCompanyName());
            experience.setMessage(input.getMessage());
            experience.setStatus(input.getStatus());
            experience.setDescription(input.getDescription());
            experience.setTypeContract(input.getTypeContract());
            experience.setDescriptionContract(input.getDescriptionContract());
            experience.setDate(input.getDate());
            experience.setFullName(input.getFullName());
            experience.setGender(input.getGender());
            experience.setNameContract(input.getNameContract());
            experience.setDuration(input.getDuration());

            saveCompanyInvitation(id, experience);

            //user
            saveUserInvitation(input.getIdTo(), experience);
        }
    }

    public void deleteInvitation(int idInvitation, int idFrom) {
        User user = userRepository.findById(idFrom).orElseThrow();

        List<InvitationModel> newListInvitation = new ArrayList<>();

        List<InvitationModel> listInvitations = user.getInvitations();

        for (InvitationModel invitationModel : listInvitations) {
            if (invitationModel.getIdInvitation() != idInvitation) {
                newListInvitation.add(invitationModel);
            }
        }

        user.setInvitations(newListInvitation);
    }

    public void deleteInvitationFromAll(int idInvitation) {
        List<User> userList = userRepository.findAll();
        for (User user : userList) {
            List<InvitationModel> newListInvitation = new ArrayList<>();

            List<InvitationModel> listInvitations = user.getInvitations();

            for (InvitationModel invitationModel : listInvitations) {
                if (invitationModel.getIdInvitation() != idInvitation) {
                    newListInvitation.add(invitationModel);
                }
            }

            user.setInvitations(newListInvitation);
        }

        userRepository.saveAll(userList);
    }

    public void finishProcess(int id, InvitationModel input) {


        User user = userRepository.findById(id).orElseThrow();
        updateInvitation(user, input);

        //user
        User candidate = userRepository.findById(input.getIdTo()).orElseThrow();
        updateInvitation(candidate, input);

    }

    public void updateInvitation(User user, InvitationModel input) {
        List<InvitationModel> list = user.getInvitations();
        InvitationModel selectedInvitation = new InvitationModel();
        int index = -1;

        for (int i = 0; i < list.size(); i++) {
            InvitationModel invitationModel = list.get(i);
            if (invitationModel.getIdInvitation() == input.getIdInvitation()) {
                index = i;
                selectedInvitation = invitationModel;
            }
        }

        if (index != -1) {
            selectedInvitation.setStatus(input.getStatus());
            selectedInvitation.setReason(input.getReason());
            selectedInvitation.setDateEnd(input.getDateEnd());

            list.set(index, selectedInvitation);

            user.setInvitations(list);

            userRepository.save(user);
        }
    }

    public void makeAnnouncement(int id, AnnounceModel input) {
        User user = userRepository.findById(id).orElseThrow();
        List<AnnounceModel> list = user.getAnnounces();
        input.setIdCompany(user.getId());
        input.setCompanyName(user.getCompanyName());
        list.add(input);
        user.setAnnounces(list);

        userRepository.save(user);
    }

    public void addComment(int idAnnounce, CommentsPost commentsPost) {
        List<User> users = userRepository.findAll();
        int idUser = -1;
        int indexUser = -1;
        int indexPost = -1;
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            List<AnnounceModel> announceModelList = user.getAnnounces();
            for (int j = 0; j < announceModelList.size(); j++) {
                AnnounceModel post = announceModelList.get(j);
                if (post.getIdAnnounce() == idAnnounce) {
                    idUser = user.getId();
                    indexPost = j;
                    indexUser = i;
                }
            }
        }

        System.out.println("bgrlzjekjkezjkezjkezjk   "+idAnnounce);

        if (indexPost != -1) {

            User user = userRepository.findById(idUser).orElseThrow();
            User userConnected = userRepository.findById(commentsPost.getIdCandidate()).orElseThrow();

            //User user = users.get(indexUser);
            AnnounceModel announceModel = user.getAnnounces().get(indexPost);

            if (user.getRole().equals("Candidate") || user.getRole().equals("Candidat")) {
                commentsPost.setUserName(userConnected.getFullName());
            } else {
                commentsPost.setUserName(userConnected.getCompanyName());
            }

            announceModel.getComments().add(commentsPost);

            user.getAnnounces().set(indexPost, announceModel);

            userRepository.save(user);
        }
    }

    public void addLike(int idAnnounce, LikesPost likesPost) {
        List<User> users = userRepository.findAll();
        int idUser = -1;
        int indexUser = -1;
        int indexPost = -1;
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            List<AnnounceModel> announceModelList = user.getAnnounces();
            for (int j = 0; j < announceModelList.size(); j++) {
                AnnounceModel post = announceModelList.get(j);
                if (post.getIdAnnounce() == idAnnounce) {
                    idUser = user.getId();
                    indexPost = j;
                    indexUser = i;
                }
            }
        }


        if (indexPost != -1) {

            User user = userRepository.findById(idUser).orElseThrow();
            User userConnected = userRepository.findById(likesPost.getIdCandidate()).orElseThrow();

            //User user = users.get(indexUser);
            AnnounceModel announceModel = user.getAnnounces().get(indexPost);

            if (user.getRole().equals("Candidate") || user.getRole().equals("Candidat")) {
                likesPost.setUserName(userConnected.getFullName());
            } else {
                likesPost.setUserName(userConnected.getCompanyName());
            }

            announceModel.getLikes().add(likesPost);

            user.getAnnounces().set(indexPost, announceModel);
            //users.set(indexUser, user);

            userRepository.save(user);
        }
    }

    public void removeLike(int idAnnounce, int idConnected) {
        List<User> users = userRepository.findAll();
        int idUser = -1;
        int indexUser = -1;
        int indexPost = -1;
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            List<AnnounceModel> announceModelList = user.getAnnounces();
            for (int j = 0; j < announceModelList.size(); j++) {
                AnnounceModel post = announceModelList.get(j);
                if (post.getIdAnnounce() == idAnnounce) {
                    idUser = user.getId();
                    indexPost = j;
                    indexUser = i;
                }
            }
        }


        if (indexPost != -1) {

            User user = userRepository.findById(idUser).orElseThrow();
            int indexToRemove = -1;
            //User user = users.get(indexUser);
            AnnounceModel announceModel = user.getAnnounces().get(indexPost);

            for (int k = 0; k < announceModel.getLikes().size(); k++) {
                LikesPost likesPost = announceModel.getLikes().get(k);
                if (likesPost.getIdCandidate() == idConnected) {
                    indexToRemove = k;
                }
            }

            if (indexToRemove != -1) {
                announceModel.getLikes().remove(indexToRemove);
            }

            user.getAnnounces().set(indexPost, announceModel);
            //users.set(indexUser, user);

            userRepository.save(user);
        }
    }

    public Page<AnnounceModel> getPaginatedAnnouncementCandidate(int page, int size) {
        List<User> userList = userRepository.findAll();
        List<AnnounceModel> newList = new ArrayList<>();
        for (User user: userList) {
            List<AnnounceModel> announceModelList = user.getAnnounces();
            newList.addAll(announceModelList);
        }

        PageRequest pageable = PageRequest.of(page - 1, 3);
        final int start = (int) pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), newList.size());

        Page<AnnounceModel> pager;

        if (start < newList.size() && start < end) {
            pager = new PageImpl<>(newList.subList(start, end), pageable, newList.size());
        } else pager = new PageImpl<>(Collections.emptyList(), pageable, newList.size());

        return pager;
    }

    public Page<AnnounceModel> getPaginatedAnnouncement(int id, int page, int size) {
        return userRepository.findPaginatedAnnouncement(id, page, size);
    }

    public Page<NotificationModel> getPaginatedNotification(int id, int page, int size) {
        return userRepository.findPaginatedNotification(id, page, size);
    }

    public Page<InvitationModel> getPaginatedInvitations(int id, int page, int size) {
        return userRepository.findPaginatedInvitations(id, page, size);
    }

    public Page<InvitationModel> getPaginatedInvitationsFiltered(FilterInvitationBody request, int page, int size) {
        return userRepository.findPaginatedInvitationsByTag(request, page, size);
    }

    public Page<InvitationModel> getPaginatedInvitationsByContract(int id, int page, int size) {
        return userRepository.findPaginatedInvitationsByContract(id, page, size);
    }

    public InvitationUser getInvitationDetail(int id, int idInvitation) {
        User user = userRepository.findById(id).orElseThrow();
        List<InvitationModel> invitations = user.getInvitations();

        InvitationModel invitationModel = null;

        for (InvitationModel invi : invitations) {
            if (invi.getIdInvitation() == idInvitation) {
                invitationModel = invi;
                break;
            }
        }

        InvitationUser invitationUser = new InvitationUser();
        invitationUser.setInvitationModel(invitationModel);

        if (invitationModel != null) {
            User candidate = getUser(invitationModel.getIdTo());
            invitationUser.setUser(candidate);
        }

        System.out.println("qlqlqlqlqlqlq   " + invitationUser);

        return invitationUser;
    }

    List<InvitationModel> getAllInvitations(int idCompany) {

        User user = userRepository.findById(idCompany).orElseThrow();
        List<InvitationModel> invitations = user.getInvitations();

        return invitations;
    }

    void acceptInvitation(int idCompany, int idInvitation) {
        User user = userRepository.findById(idCompany).orElseThrow();
        List<InvitationModel> invitations = user.getInvitations();

        boolean isPresent = false;
        int index = -1;

        for (int i = 0; i < invitations.size(); i++) {
            if (invitations.get(i).getIdInvitation() == idInvitation) {
                isPresent = true;
                index = i;
            }
        }

        if (isPresent) {
            invitations.get(index).setAccepted(true);
            ;
            user.setInvitations(invitations);
        }
        userRepository.save(user);
    }

    public Page<SearchHistory> searchCandidate(String word, int id, int page, int size) {

        List<User> candidates = userRepository.findAll();

        List<User> newUsers = new ArrayList<>();
        for (User c : candidates) {
            if (c.getRole().equals("Candidate") && c.getFullName().toLowerCase().contains(word)) {
                newUsers.add(c);
            }
        }

        List<User> combined;

        if (word.isEmpty()) combined = candidates;
        else combined = newUsers;

        List<SearchHistory> list = new ArrayList<>();
        for (User c : combined) {
            SearchHistory searchHistory = new SearchHistory();
            searchHistory.setIdUser(c.getId());
            searchHistory.setGender(c.getSexe());
            searchHistory.setFullName(c.getFullName());

            List<Experience> exp = c.getExperiences();
            if (!exp.isEmpty()) {
                String date = exp.get(0).getDateStart();
                if (date.contains(",")) {
                    String[] dates = date.split(", ");
                    if (dates.length > 0) {
                        int year = Integer.parseInt(dates[2]);
                        int currentYear = Year.now().getValue();

                        int diff = currentYear - year;
                        if (diff > 0) {
                            searchHistory.setExperience(diff + " years experiences");
                        }
                    }
                }

            }

            list.add(searchHistory);
        }


        PageRequest pageable = PageRequest.of(page - 1, 3);
        final int start = (int) pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), newUsers.size());

        return new PageImpl<>(list.subList(start, end), pageable, list.size());
    }

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

    public Page<Experience> getPaginatedExperiences(int id, int page, int size) {
        return userRepository.findPaginatedExperiences(id, page, size);
    }

    public Page<Education> getPaginatedEducations(int id, int page, int size) {
        return userRepository.findPaginatedEducations(id, page, size);
    }

    public List<Experience> getAllExperience(int id) {
        User user = userRepository.findById(id).orElseThrow();
        return user.getExperiences();
    }

    public void removeSearchHistory(int idUserConnected, int idUserToDelete) {
        User user = userRepository.findById(idUserConnected).orElseThrow();
        List<SearchHistory> list = user.getSearchHistories();

        boolean isPresent = false;
        int index = -1;

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getIdUser() == idUserToDelete) {
                isPresent = true;
                index = i;
            }
        }

        if (isPresent) {
            list.remove(index);
            user.setSearchHistories(list);
        }
        userRepository.save(user);
    }

    public void removeExperience(int idUser, int idExperience) {
        User user = userRepository.findById(idUser).orElseThrow();
        List<Experience> list = user.getExperiences();

        boolean isPresent = false;
        int index = -1;

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == idExperience) {
                isPresent = true;
                index = i;
            }
        }

        if (isPresent) {
            list.remove(index);
            user.setExperiences(list);
        }
        userRepository.save(user);
    }

    public List<String> getCompaniesValidated() {
        List<User> users = userRepository.findAll();
        List<String> companyNames = new ArrayList<>();
        for (User user : users) {
            if (user.getRole().equals("Company") || user.getRole().equals("Entreprise")) {
                /*if (user.isVerified()) {
                    companyNames.add(user.getCompanyName());
                }*/
                companyNames.add(user.getCompanyName());
            }
        }
        return companyNames;
    }

    public void removeEducation(int idUser, int idEducation) {
        User user = userRepository.findById(idUser).orElseThrow();
        List<Education> list = user.getEducation();

        boolean isPresent = false;
        int index = -1;

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == idEducation) {
                isPresent = true;
                index = i;
            }
        }

        if (isPresent) {
            list.remove(index);
            user.setEducation(list);
        }
        userRepository.save(user);
    }

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

    public User authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return userRepository.findByEmail(input.getEmail()).orElseThrow();
    }

    public User authenticateByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow();
    }

    public boolean hasExipred(LocalDateTime expiryDateTime) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        return expiryDateTime.isAfter(currentDateTime);
    }

    public void saveCandidateStatus(ValidationStatus validationStatus) {
        User user = userRepository.findById(validationStatus.getId()).orElseThrow();
        user.setValidationStatus(validationStatus);
        user.getValidationStepStatus().add(validationStatus);
        userRepository.save(user);
    }

    public ValidationStatus getStatusCandidateValidation(int id) {
        User user = userRepository.findById(id).orElseThrow();
        return user.getValidationStatus();
    }

    public List<ValidationStatus> getListStatusCandidateValidation(int id) {
        User user = userRepository.findById(id).orElseThrow();
        return user.getValidationStepStatus();
    }
}
