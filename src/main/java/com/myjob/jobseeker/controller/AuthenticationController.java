package com.myjob.jobseeker.controller;

import com.myjob.jobseeker.dtos.*;
import com.myjob.jobseeker.model.*;
import com.myjob.jobseeker.model.post.CommentsPost;
import com.myjob.jobseeker.model.post.LikesPost;
import com.myjob.jobseeker.services.*;
import org.bson.types.ObjectId;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RequestMapping("/auth")
@RestController
@CrossOrigin(origins = "*")  // Allows requests from any domain (or specify your Android IP)
public class AuthenticationController {

    private final JwtService jwtService;
    private final EmailService emailService;
    private final PasswordResetService passwordResetService;
    private final AuthenticationService authenticationService;
    private final FileStorageService fileStorageService;
    private final SkillExtractorService skillExtractorService;

    public AuthenticationController(JwtService jwtService,
                                    SkillExtractorService skillExtractorService,
                                    AuthenticationService authenticationService, EmailService emailService, PasswordResetService passwordResetService,
                                    FileStorageService fileStorageService) {
        this.jwtService = jwtService;
        this.skillExtractorService = skillExtractorService;
        this.authenticationService = authenticationService;
        this.passwordResetService = passwordResetService;
        this.emailService = emailService;
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/extract")
    public Map<String, List<String>> extract(@RequestParam String description) {
        return skillExtractorService.extract(description);
    }

    ///////////////////////////////////////////////////////////////////////////
    // VERIFICATION
    ///////////////////////////////////////////////////////////////////////////
    @PostMapping("/verifyAccountCompany")
    public ResponseEntity<ExperienceResponse> verifyAccountCompany(@RequestParam int id) {
        authenticationService.verifyAccountCompany(id);
        ExperienceResponse experienceResponse = new ExperienceResponse();
        experienceResponse.setId(1);
        experienceResponse.setMessage("saved successfully");

        return ResponseEntity.ok(experienceResponse);
    }

    @PostMapping("/validate-profile")
    public ResponseEntity<EmailResponse> validateProfile(@RequestParam String email) {
        emailService.sendTokenValidation(email);
        EmailResponse emailResponse = new EmailResponse();
        emailResponse.setId(1);
        emailResponse.setMessage("Check your email to validate your account");

        return ResponseEntity.ok(emailResponse);
    }

    @PostMapping("/validate-profile-candidate")
    public ResponseEntity<EmailResponse> validateCandidateProfile(@RequestBody ValidationStatus validationStatus) {
        authenticationService.saveCandidateStatus(validationStatus);

        if (validationStatus.getTypeValidation().equals("interview")) {
            emailService.sendLinkValidation(validationStatus.getEmail());
        }

        EmailResponse emailResponse = new EmailResponse();
        emailResponse.setId(1);
        emailResponse.setMessage("Check your email to validate your account");

        return ResponseEntity.ok(emailResponse);
    }


    @GetMapping("/statusCandidateValidation")
    public ResponseEntity<ValidationStatus> getListStatusCandidateValidation(@RequestParam int id) {
        ValidationStatus validationStatus = authenticationService.getStatusCandidateValidation(id);
        return ResponseEntity.ok(validationStatus);
    }

    @GetMapping("/statusListCandidateValidation")
    public ResponseEntity<List<ValidationStatus>> getStatusCandidateValidation(@RequestParam int id) {
        List<ValidationStatus> validationStatus = authenticationService.getListStatusCandidateValidation(id);
        return ResponseEntity.ok(validationStatus);
    }

    ///////////////////////////////////////////////////////////////////////////
    // NOTIFICATION
    ///////////////////////////////////////////////////////////////////////////
    @PostMapping("/updatetoken")
    public ResponseEntity<ExperienceResponse> updateToken(@RequestParam int id, @RequestParam String token) {
        authenticationService.updateToken(id, token);
        ExperienceResponse experienceResponse = new ExperienceResponse();
        experienceResponse.setId(1);
        experienceResponse.setMessage("saved successfully");

        return ResponseEntity.ok(experienceResponse);
    }

    @PostMapping("/sendnotification")
    public ResponseEntity<ExperienceResponse> sendNotification(@RequestBody NotificationMessage notificationMessage) {
        String res = authenticationService.sendNotification(notificationMessage);

        ExperienceResponse experienceResponse = new ExperienceResponse();
        experienceResponse.setId(1);
        experienceResponse.setMessage(res);

        return ResponseEntity.ok(experienceResponse);
    }

    @GetMapping("/getCompanyNotifications")
    public ResponseEntity<Page<NotificationModel>> getCompanyNotifications(
            @RequestParam int id,
            @RequestParam int page,
            @RequestParam int size) {

        Page<NotificationModel> invitation = authenticationService.getPaginatedNotification(id, page, size);


        return ResponseEntity.ok(invitation);
    }


    ///////////////////////////////////////////////////////////////////////////
    // SUBSCRIPTION
    ///////////////////////////////////////////////////////////////////////////
    @PostMapping("/signup")
    public ResponseEntity<RegistrationResponse> register(@RequestBody RegisterUserDto registerUserDto) {

        UserResponse registeredUser = authenticationService.signup(registerUserDto);

        RegistrationResponse registrationResponse = new RegistrationResponse();

        if (registeredUser.getMessage() != null && !registeredUser.getMessage().isEmpty()) {

            registrationResponse.setMessageError(registeredUser.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(registrationResponse);
        } else {

            String jwtToken = jwtService.generateToken(registeredUser.getUser());

            registrationResponse.setToken(jwtToken);
            registrationResponse.setUser(registeredUser.getUser());
            registrationResponse.setExpiresIn(jwtService.getExpirationTime());
            return ResponseEntity.ok(registrationResponse);
        }
    }

    @PostMapping("/verification")
    public ResponseEntity<LoginResponse> verification(@RequestParam String email) {

        String error = "";
        try {
            authenticationService.authenticateByEmail(email);
        } catch (Exception exception) {
            error = exception.getMessage();
        }
        LoginResponse loginResponse = new LoginResponse();

        if (error.equals("No value present")) {
            loginResponse.setMessageError("good to go");
        } else {
            //non
            loginResponse.setMessageError("Email already exist");
        }
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
        User authenticatedUser;
        String error = "";
        try {
            authenticatedUser = authenticationService.authenticate(loginUserDto);
        } catch (Exception exception) {
            authenticatedUser = new User();
            error = exception.getMessage();
        }

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse();

        if (!Objects.equals(error, "")) {
            loginResponse.setMessageError(error);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(loginResponse);
        } else {
            loginResponse.setToken(jwtToken);
            loginResponse.setUser(authenticatedUser);
            loginResponse.setExpiresIn(jwtService.getExpirationTime());
            return ResponseEntity.ok(loginResponse);
        }

    }

    @PostMapping("/forgot-password")
    public ResponseEntity<EmailResponse> forgotPassword(@RequestParam String email) {

        String token = passwordResetService.createPasswordResetTokenForUser(email);
        emailService.sendResetToken(email, token);
        EmailResponse emailResponse = new EmailResponse();
        emailResponse.setId(1);
        emailResponse.setMessage("Password reset link sent to your email.");
        return ResponseEntity.ok(emailResponse);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<PasswordResponse> resetPassword(@RequestParam String token, @RequestParam String newPassword) {

        String message = "";
        if (passwordResetService.validatePasswordResetToken(token)) {
            passwordResetService.updatePassword(token, newPassword);
            message = "Password successfully reset.";
        } else message = "Invalid token or token expired.";

        PasswordResponse passwordResponse = new PasswordResponse();
        passwordResponse.setId(1);
        passwordResponse.setMessage(message);

        return ResponseEntity.ok(passwordResponse);
    }

    ///////////////////////////////////////////////////////////////////////////
    // EXPERIENCES
    ///////////////////////////////////////////////////////////////////////////
    @PostMapping("/add")
    public ResponseEntity<ExperienceResponse> register(@RequestBody ExperienceDto experienceDto) {

        authenticationService.saveExperience(experienceDto);

        ExperienceResponse experienceResponse = new ExperienceResponse();
        experienceResponse.setId(1);
        experienceResponse.setMessage("saved successfully");

        return ResponseEntity.ok(experienceResponse);
    }
    
    @GetMapping("/getAllExp")
    public ResponseEntity<List<Experience>> getExperiences(@RequestParam int id) {

        List<Experience> experience = authenticationService.getAllExperience(id);

        return ResponseEntity.ok(experience);
    }

    @GetMapping("/getAllExperience")
    public ResponseEntity<Page<Experience>> getExperiencesPages(
            @RequestParam int id,
            @RequestParam int page,
            @RequestParam int size) {

        Page<Experience> experiences = authenticationService.getPaginatedExperiences(id, page, size);

        return ResponseEntity.ok(experiences);
    }

    @PostMapping("/removeExperience")
    public ResponseEntity<ExperienceResponse> removeExperience(@RequestParam int id, @RequestParam int experienceId) {
        authenticationService.removeExperience(id, experienceId);

        ExperienceResponse experienceResponse = new ExperienceResponse();
        experienceResponse.setId(1);
        experienceResponse.setMessage("removed successfully");

        return ResponseEntity.ok(experienceResponse);
    }

    ///////////////////////////////////////////////////////////////////////////
    // INVITATIONS
    ///////////////////////////////////////////////////////////////////////////
    @GetMapping("/getCompanyInvitations")
    public ResponseEntity<Page<InvitationModel>> getCompanyInvitations(
            @RequestParam int id,
            @RequestParam int page,
            @RequestParam int size) {

        Page<InvitationModel> invitation = authenticationService.getPaginatedInvitations(id, page, size);

        return ResponseEntity.ok(invitation);
    }

    @PostMapping("/getFilteredInvitation")
    public ResponseEntity<Page<InvitationModel>> getFilteredInvitation(
            @RequestBody FilterInvitationBody request,
            @RequestParam int page,
            @RequestParam int size
    ) {
        Page<InvitationModel> response = authenticationService.getPaginatedInvitationsFiltered(request, page, size);

        return ResponseEntity.ok(response);
    }

    public void sendNotificationAfterSendInvitation(NotificationMessage notificationMessage) {
        String res = authenticationService.sendNotification(notificationMessage);
    }

    @PostMapping("/sendInvitation")
    public ResponseEntity<ExperienceResponse> sendInvitation(@RequestBody InvitationDto invitationDto) {

        authenticationService.sendInvitation(invitationDto.getIdConnected(), invitationDto.getInvitationModel());
        int idInvitation = invitationDto.getInvitationModel().getIdInvitation();
        int idConnected = invitationDto.getIdConnected();

        User user = authenticationService.getUser(idConnected);

        Map<String, String> data = new HashMap<>();
        data.put("idInvitation", idInvitation+"");

        NotificationMessage notificationMessage = new NotificationMessage();
        notificationMessage.setRecipientToken(user.getFcmToken());
        notificationMessage.setData(data);

        sendNotificationAfterSendInvitation(notificationMessage);

        ExperienceResponse experienceResponse = new ExperienceResponse();
        experienceResponse.setId(1);
        experienceResponse.setMessage("saved successfully");

        return ResponseEntity.ok(experienceResponse);
    }

    @GetMapping("/getInvitationById")
    public ResponseEntity<InvitationUser> getInvitationById(
            @RequestParam int idUser,
            @RequestParam int idInvitation) {

        InvitationUser experiences = authenticationService.getInvitationDetail(idUser, idInvitation);

        return ResponseEntity.ok(experiences);
    }

    @PostMapping("/finishProcess")
    public ResponseEntity<InvitationDto> finishProcess(@RequestBody InvitationDto invitationDto) {
        //getIdConnected() => company
        authenticationService.finishProcess(invitationDto.getIdConnected(), invitationDto.getInvitationModel());

        return ResponseEntity.ok(invitationDto);
    }

    @MessageMapping("/requestInvitations")
    @SendTo("/topic/invitations")
    public Page<InvitationModel> handleRequest(Map<String, Integer> request) {

        int id = request.getOrDefault("id", 0);
        int page = request.getOrDefault("page", 1);
        int size = request.getOrDefault("size", 10);
        return authenticationService.getPaginatedInvitations(id, page, size);
    }

    @GetMapping("/getInvitationDetail")
    public ResponseEntity<InvitationUser> getInvitationDetail(
            @RequestParam int id,
            @RequestParam int idInvitation
    ) {

        InvitationUser invitation = authenticationService.getInvitationDetail(id, idInvitation);

        return ResponseEntity.ok(invitation);
    }

    @GetMapping("/getInvitations")
    public ResponseEntity<Page<InvitationModel>> getInvitationsPages(
            @RequestParam int id,
            @RequestParam int page,
            @RequestParam int size) {

        Page<InvitationModel> experiences = authenticationService.getPaginatedInvitations(id, page, size);

        return ResponseEntity.ok(experiences);
    }

    @GetMapping("/getInvitationsByTag")
    public ResponseEntity<Page<InvitationModel>> getInvitationsByTag(
            @RequestParam int id,
            @RequestParam int page,
            @RequestParam int size) {

        Page<InvitationModel> experiences = authenticationService.getPaginatedInvitationsByContract(id, page, size);



        return ResponseEntity.ok(experiences);
    }

    @GetMapping("/getCompaniesValidated")
    public ResponseEntity<List<String>> getCompaniesValidated() {

        List<String> experiences = authenticationService.getCompaniesValidated();
        return ResponseEntity.ok(experiences);
    }

    @PostMapping("/acceptRejectInvitation")
    public ResponseEntity<ExperienceResponse> acceptRejectInvitation(@RequestBody InvitationDto invitationDto) {

        String status = authenticationService.acceptRejectInvitation(invitationDto);

        ExperienceResponse experienceResponse = new ExperienceResponse();
        experienceResponse.setId(1);
        experienceResponse.setMessage(status);

        return ResponseEntity.ok(experienceResponse);
    }

    @PostMapping("/deleteInvitation")
    public ResponseEntity<ExperienceResponse> deleteInvitation(@RequestParam int idInvitation, @RequestParam int idInvitationFrom) {

        authenticationService.deleteInvitation(idInvitation, idInvitationFrom);

        ExperienceResponse experienceResponse = new ExperienceResponse();
        experienceResponse.setId(1);
        experienceResponse.setMessage("removed successfully");

        return ResponseEntity.ok(experienceResponse);
    }

    ///////////////////////////////////////////////////////////////////////////
    // USER
    ///////////////////////////////////////////////////////////////////////////
    @PostMapping("/getByCriteria")
    public ResponseEntity<Page<User>> getByCriteria(
            @RequestBody Criteria criteria,
            @RequestParam int page,
            @RequestParam int size
    ) {
        Page<User> experience = authenticationService.getByCriteria(criteria, page, size);
        return ResponseEntity.ok(experience);
    }

    @GetMapping("/getNewCandidate")
    public ResponseEntity<Page<User>> getNewCandidate(
            @RequestParam int id,
            @RequestParam int page,
            @RequestParam int size) {

        return ResponseEntity.ok(authenticationService.getNewCandidate(id, page, size));
    }

    @GetMapping("/getAllCandidate")
    public ResponseEntity<Page<User>> getAllCandidate(
            @RequestParam int id,
            @RequestParam int page,
            @RequestParam int size) {

        return ResponseEntity.ok(authenticationService.getUsers(id, page, size));
    }

    @GetMapping("/getAllFavoritesCandidates")
    public ResponseEntity<Page<User>> getUsersFavorites(
            @RequestParam int id,
            @RequestParam int page,
            @RequestParam int size) {

        return ResponseEntity.ok(authenticationService.getUsersFavorites(id, page, size));
    }

    @PostMapping("/updateuser")
    public ResponseEntity<ExperienceResponse> updateUser(
            @RequestParam String lang,
            @RequestBody PersonalInfoDto personalInfoDto
    ) {

        authenticationService.savePersonal(personalInfoDto);

        ExperienceResponse experienceResponse = new ExperienceResponse();
        experienceResponse.setId(1);
        experienceResponse.setMessage("saved successfully");

        return ResponseEntity.ok(experienceResponse);
    }

    @PostMapping("/updateCandidateProfessional")
    public ResponseEntity<ExperienceResponse> updateCandidateProfessional(
            @RequestBody ProfessionalStatus professionalStatus
    ) {

        authenticationService.saveProfessionalInfo(professionalStatus);

        ExperienceResponse experienceResponse = new ExperienceResponse();
        experienceResponse.setId(1);
        experienceResponse.setMessage("saved successfully");

        return ResponseEntity.ok(experienceResponse);
    }

    @PostMapping("/updateCandidateSkills")
    public ResponseEntity<ExperienceResponse> updateCandidateSkills(
            @RequestBody CandidateSkills candidateSkills
    ) {

        authenticationService.saveSkillsInfo(candidateSkills);

        ExperienceResponse experienceResponse = new ExperienceResponse();
        experienceResponse.setId(1);
        experienceResponse.setMessage("saved successfully");

        return ResponseEntity.ok(experienceResponse);
    }

    @PostMapping("/updateCandidateCompleted")
    public ResponseEntity<ExperienceResponse> updateCompleted(@RequestParam int id) {

        authenticationService.completeUpdated(id);

        ExperienceResponse experienceResponse = new ExperienceResponse();
        experienceResponse.setId(1);
        experienceResponse.setMessage("saved successfully");

        return ResponseEntity.ok(experienceResponse);
    }

    @PostMapping("/updatecompany")
    public ResponseEntity<ExperienceResponse> updateCompany(@RequestBody CompanyInfoDto companyInfoDto) {
        authenticationService.saveCompanyInfo(companyInfoDto);

        ExperienceResponse experienceResponse = new ExperienceResponse();
        experienceResponse.setId(1);
        experienceResponse.setMessage("saved successfully");

        return ResponseEntity.ok(experienceResponse);
    }

    @GetMapping("/getUser")
    public ResponseEntity<User> getUser(@RequestParam int id) {
        User user = authenticationService.getUser(id);

        return ResponseEntity.ok(user);
    }

    ///////////////////////////////////////////////////////////////////////////
    // SEARCH
    ///////////////////////////////////////////////////////////////////////////
    @GetMapping("/getUserFiltered")
    public ResponseEntity<Page<User>> getUserFiltered(
            @RequestParam String word,
            @RequestParam int id,
            @RequestParam int page,
            @RequestParam int size) {

        return ResponseEntity.ok(authenticationService.getUserFiltered(word, id, page, size));
    }

    @PostMapping("/saveSearchHistory")
    public ResponseEntity<ExperienceResponse> saveSearchHistory(
            @RequestParam int idUserConnected,
            @RequestBody SearchHistory searchHistory
    ) {

        authenticationService.saveSearchHistory(idUserConnected, searchHistory);

        ExperienceResponse experienceResponse = new ExperienceResponse();
        experienceResponse.setId(1);
        experienceResponse.setMessage("saved successfully");

        return ResponseEntity.ok(experienceResponse);
    }

    @GetMapping("/getAllSearchHistory")
    public ResponseEntity<Page<SearchHistory>> getAllSearch(
            @RequestParam int id,
            @RequestParam int page,
            @RequestParam int size) {

        return ResponseEntity.ok(authenticationService.getPaginatedSearches(id, page, size));
    }

    @GetMapping("/searchCandidate")
    public ResponseEntity<Page<SearchHistory>> searchCandidate(
            @RequestParam String word,
            @RequestParam int id,
            @RequestParam int page,
            @RequestParam int size
    ) {

        Page<SearchHistory> users = authenticationService.searchCandidate(word, id, page, size);

        return ResponseEntity.ok(users);
    }

    @PostMapping("/removeSearchHistory")
    public ResponseEntity<ExperienceResponse> removeSearchHistory(
            @RequestParam int idUserConnected, @RequestParam int idUserToDelete
    ) {


        authenticationService.removeSearchHistory(idUserConnected, idUserToDelete);

        ExperienceResponse experienceResponse = new ExperienceResponse();
        experienceResponse.setId(1);
        experienceResponse.setMessage("removed successfully");

        return ResponseEntity.ok(experienceResponse);
    }

    ///////////////////////////////////////////////////////////////////////////
    // ANNOUCEMEMNT
    ///////////////////////////////////////////////////////////////////////////
    @PostMapping("/makeAnnouncement")
    public ResponseEntity<ExperienceResponse> makeAnnouncement(
            @RequestParam int idUserConnected,
            @RequestBody AnnounceModel announceModel) {

        authenticationService.makeAnnouncement(idUserConnected, announceModel);

        ExperienceResponse experienceResponse = new ExperienceResponse();
        experienceResponse.setId(1);
        experienceResponse.setMessage("saved successfully");

        return ResponseEntity.ok(experienceResponse);
    }

    @PostMapping("/addComment")
    public ResponseEntity<ExperienceResponse> addComment(
            @RequestParam int idAnnounce,
            @RequestBody CommentsPost commentsPost) {

        authenticationService.addComment(idAnnounce, commentsPost);

        ExperienceResponse experienceResponse = new ExperienceResponse();
        experienceResponse.setId(1);
        experienceResponse.setMessage("saved successfully");

        return ResponseEntity.ok(experienceResponse);
    }

    @PostMapping("/addLikes")
    public ResponseEntity<ExperienceResponse> addLikes(
            @RequestParam int idAnnounce,
            @RequestBody LikesPost likesPost) {

        authenticationService.addLike(idAnnounce, likesPost);

        ExperienceResponse experienceResponse = new ExperienceResponse();
        experienceResponse.setId(1);
        experienceResponse.setMessage("saved successfully");

        return ResponseEntity.ok(experienceResponse);
    }

    @PostMapping("/removeLike")
    public ResponseEntity<ExperienceResponse> removeLike(
            @RequestParam int idAnnounce,
            @RequestParam int idConnected) {

        authenticationService.removeLike(idAnnounce, idConnected);

        ExperienceResponse experienceResponse = new ExperienceResponse();
        experienceResponse.setId(1);
        experienceResponse.setMessage("saved successfully");

        return ResponseEntity.ok(experienceResponse);
    }

    @GetMapping("/getCompanyAnnouncements")
    public ResponseEntity<Page<AnnounceModel>> getCompanyAnnouncements(
            @RequestParam int id,
            @RequestParam int page,
            @RequestParam int size) {

        Page<AnnounceModel> invitation = authenticationService.getPaginatedAnnouncement(id, page, size);


        return ResponseEntity.ok(invitation);
    }

    ///////////////////////////////////////////////////////////////////////////
    // EDUCATION
    ///////////////////////////////////////////////////////////////////////////
    @GetMapping("/getAllEducation")
    public ResponseEntity<Page<Education>> getAllEducations(
            @RequestParam int id,
            @RequestParam int page,
            @RequestParam int size
    ) {

        Page<Education> educations = authenticationService.getPaginatedEducations(id, page, size);


        return ResponseEntity.ok(educations);
    }

    @GetMapping("/getAllEduc")
    public ResponseEntity<List<Education>> getAllEduc(@RequestParam int id) {

        List<Education> educations = authenticationService.getAllEducations(id);

        return ResponseEntity.ok(educations);
    }

    @PostMapping("/addEducation")
    public ResponseEntity<ExperienceResponse> saveEducation(@RequestBody EducationDto educationDto) {

        System.err.println("grfzgetetexperienceDto   " + educationDto);

        authenticationService.saveEducation(educationDto);

        ExperienceResponse experienceResponse = new ExperienceResponse();
        experienceResponse.setId(1);
        experienceResponse.setMessage("saved successfully");

        return ResponseEntity.ok(experienceResponse);
    }

    @PostMapping("/removeEducation")
    public ResponseEntity<ExperienceResponse> removeEducation(@RequestParam int id, @RequestParam int educationId) {


        authenticationService.removeEducation(id, educationId);

        ExperienceResponse experienceResponse = new ExperienceResponse();
        experienceResponse.setId(1);
        experienceResponse.setMessage("removed successfully");

        return ResponseEntity.ok(experienceResponse);
    }

    ///////////////////////////////////////////////////////////////////////////
    // FAVORITES
    ///////////////////////////////////////////////////////////////////////////
    @PostMapping("/updatefavorite")
    public ResponseEntity<ExperienceResponse> updateFavorite(@RequestParam int idUserConnected, @RequestParam int candidateId) {


        authenticationService.updateFavorite(idUserConnected, candidateId);

        ExperienceResponse experienceResponse = new ExperienceResponse();
        experienceResponse.setId(1);
        experienceResponse.setMessage("saved successfully");

        return ResponseEntity.ok(experienceResponse);
    }

    @GetMapping("/getFavorites")
    public ResponseEntity<Page<FavoriteModel>> getFavoritesPages(
            @RequestParam int id,
            @RequestParam int page,
            @RequestParam int size) {

        Page<FavoriteModel> experiences = authenticationService.getPaginatedFavorites(id, page, size);

        return ResponseEntity.ok(experiences);
    }

    ///////////////////////////////////////////////////////////////////////////
    // FILES
    ///////////////////////////////////////////////////////////////////////////
    @PostMapping("/uploadCV")
    public ResponseEntity<ExperienceResponse> uploadFile(@RequestParam("file") MultipartFile file) {
        ExperienceResponse experienceResponse = new ExperienceResponse();
        experienceResponse.setId(1);

        try {
            // Save the file locally
            System.out.println("file name rzghlrghlzr  "+file.getName());
            String filePath = fileStorageService.storeFile(file);
            file.transferTo(new File(filePath));
            experienceResponse.setMessage(file.getOriginalFilename());
        } catch (Exception e) {
            experienceResponse.setMessage("File upload failed: " + e.getMessage());
        }

        return ResponseEntity.ok(experienceResponse);
    }

    @GetMapping("/isExisted")
    public ResponseEntity<FileExistingResponse> isExisted(@RequestParam String fileName) {
        FileExistingResponse experienceResponse = new FileExistingResponse();
        experienceResponse.setId(1);

        boolean filePath = fileStorageService.isExisted(fileName);
        experienceResponse.setExisted(filePath);

        return ResponseEntity.ok(experienceResponse);
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> retrieveFile(@RequestParam String fileName) {
        return fileStorageService.retrieveFile(fileName);
    }

    ///////////////////////////////////////////////////////////////////////////
    // FILES
    ///////////////////////////////////////////////////////////////////////////
    @PostMapping("/upload")
    public String uploadFiles(@RequestParam("file") MultipartFile file) throws IOException {
        StoredFile storedFile = new StoredFile();
        storedFile.setFilename(file.getOriginalFilename());
        storedFile.setContentType(file.getContentType());
        storedFile.setData(file.getBytes());
        return "File uploaded successfully!";
    }

}
