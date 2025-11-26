package com.myjob.jobseeker.controller;

import com.myjob.jobseeker.dtos.*;
import com.myjob.jobseeker.model.*;
import com.myjob.jobseeker.services.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Objects;

@RequestMapping("/auth")
@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final JwtService jwtService;
    private final EmailService emailService;
    private final PasswordResetService passwordResetService;
    private final AuthService authenticationService;

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
    // SUBSCRIPTION / AUTH
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

}