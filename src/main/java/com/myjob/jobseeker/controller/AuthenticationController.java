package com.myjob.jobseeker.controller;

import com.myjob.jobseeker.dtos.*;
import com.myjob.jobseeker.model.*;
import com.myjob.jobseeker.services.*;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@RequestMapping("/auth")
@RestController
@CrossOrigin(origins = "*")  // Allows requests from any domain (or specify your Android IP)
public class AuthenticationController {

    private final JwtService jwtService;
    private final EmailService emailService;
    private final PasswordResetService passwordResetService;
    private final AuthenticationService authenticationService;
    private final FileStorageService fileStorageService;

    public AuthenticationController(JwtService jwtService,
                                    AuthenticationService authenticationService, EmailService emailService, PasswordResetService passwordResetService,
                                    FileStorageService fileStorageService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
        this.passwordResetService = passwordResetService;
        this.emailService = emailService;
        this.fileStorageService = fileStorageService;
    }

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
            System.err.println("lzapzaoiazioazio   " + authenticatedUser);
        } catch (Exception exception) {
            authenticatedUser = new User();
            error = exception.getMessage();
        }

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse();

        if (error != "") {
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

    @PostMapping("/validate-profile")
    public ResponseEntity<EmailResponse> validateProfile(@RequestParam String email) {
        System.err.println("emaillll  " + email);
        emailService.sendTokenValidation(email);
        EmailResponse emailResponse = new EmailResponse();
        emailResponse.setId(1);
        emailResponse.setMessage("Check your email to validate your account");

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

    @PostMapping("/add")
    public ResponseEntity<ExperienceResponse> register(@RequestBody ExperienceDto experienceDto) {

        System.err.println("grfzgetetexperienceDto   " + experienceDto.toString());

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

    @PostMapping("/getByCriteria")
    public ResponseEntity<List<User>> getByCriteria(@RequestBody Criteria criteria) {

        System.out.println("fffffffffffffffffffff    " + criteria);

        List<User> experience = authenticationService.getByCriteria(criteria);

        System.out.println("fffffffffffffffffffff    " + experience.size());

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

    @GetMapping("/getCompanyInvitations")
    public ResponseEntity<Page<InvitationModel>> getCompanyInvitations(
            @RequestParam int id,
            @RequestParam int page,
            @RequestParam int size) {

        Page<InvitationModel> invitation = authenticationService.getPaginatedInvitations(id, page, size);

        return ResponseEntity.ok(invitation);
    }

    @GetMapping("/getAllJobs1")
    public ResponseEntity<Page<User>> getJobsPages(
            @RequestParam int page,
            @RequestParam int size) {

        return ResponseEntity.ok(authenticationService.getUsers1(page, size));
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

    @PostMapping("/sendInvitation")
    public ResponseEntity<ExperienceResponse> sendInvi(@RequestBody InvitationDto invitationDto) {

        authenticationService.sendInvitaion(invitationDto.getIdConnected(), invitationDto.getInvitationModel());

        ExperienceResponse experienceResponse = new ExperienceResponse();
        experienceResponse.setId(1);
        experienceResponse.setMessage("saved successfully");

        return ResponseEntity.ok(experienceResponse);
    }

    @GetMapping("/getAllJobs")
    public ResponseEntity<Page<User>> getJobsPages1(
            @RequestParam int page,
            @RequestParam int size) {

        return ResponseEntity.ok(authenticationService.getUsers(10, page, size));
    }


    @GetMapping("/getAllFavoritesCandidates")
    public ResponseEntity<Page<User>> getUsersFavorites(
            @RequestParam int id,
            @RequestParam int page,
            @RequestParam int size) {

        return ResponseEntity.ok(authenticationService.getUsersFavorites(id, page, size));
    }


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


    @PostMapping("/addEducation")
    public ResponseEntity<ExperienceResponse> saveEducation(@RequestBody EducationDto educationDto) {

        System.err.println("grfzgetetexperienceDto   " + educationDto);

        authenticationService.saveEducation(educationDto);

        ExperienceResponse experienceResponse = new ExperienceResponse();
        experienceResponse.setId(1);
        experienceResponse.setMessage("saved successfully");

        return ResponseEntity.ok(experienceResponse);
    }

    @PostMapping("/updateuser")
    public ResponseEntity<ExperienceResponse> updateUser(@RequestBody PersonalInfoDto personalInfoDto) {


        System.err.println("gjkrgjrnzgrzgnzrlgnzrl   " + personalInfoDto.getAvailability());

        authenticationService.savePersonal(personalInfoDto);

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

    @GetMapping("/getInvitations")
    public ResponseEntity<Page<InvitationModel>> getInvitationsPages(
            @RequestParam int id,
            @RequestParam int page,
            @RequestParam int size) {

        Page<InvitationModel> experiences = authenticationService.getPaginatedInvitations(id, page, size);

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

    @PostMapping("/removeEducation")
    public ResponseEntity<ExperienceResponse> removeEducation(@RequestParam int id, @RequestParam int educationId) {


        authenticationService.removeEducation(id, educationId);

        ExperienceResponse experienceResponse = new ExperienceResponse();
        experienceResponse.setId(1);
        experienceResponse.setMessage("removed successfully");

        return ResponseEntity.ok(experienceResponse);
    }

    @GetMapping("/getUser")
    public ResponseEntity<User> getUser(@RequestParam int id) {
        User user = authenticationService.getUser(id);

        return ResponseEntity.ok(user);
    }

    @PostMapping("/uploadCV")
    public ResponseEntity<ExperienceResponse> uploadFile(@RequestParam("file") MultipartFile file) {
        ExperienceResponse experienceResponse = new ExperienceResponse();
        experienceResponse.setId(1);

        try {
            // Save the file locally
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
        System.err.println("fjenafkeanfea   " + filePath);
        experienceResponse.setExisted(filePath);

        return ResponseEntity.ok(experienceResponse);
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> retrieveFile(@RequestParam String fileName) {
        return fileStorageService.retrieveFile(fileName);
    }
}
