package com.myjob.jobseeker.controller;

import com.myjob.jobseeker.dtos.*;
import com.myjob.jobseeker.model.CandidateSkills;
import com.myjob.jobseeker.model.ProfessionalStatus;
import com.myjob.jobseeker.model.User;
import com.myjob.jobseeker.services.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/auth")
@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

    private final AuthService authenticationService;

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

    @PostMapping("/countDownTrialUser")
    public ResponseEntity<ExperienceResponse> countDownTrial(@RequestParam int idUser) {
        authenticationService.countDownTrial(idUser);
        ExperienceResponse experienceResponse = new ExperienceResponse();
        experienceResponse.setId(1);
        experienceResponse.setMessage("saved successfully");

        return ResponseEntity.ok(experienceResponse);
    }

    @GetMapping("/getUserServiceFiltered")
    public ResponseEntity<Page<User>> getUserServiceFiltered(
            @RequestParam String word,
            @RequestParam int page,
            @RequestParam int size) {

        return ResponseEntity.ok(authenticationService.getUserServiceFiltered(word, page, size));
    }

    @GetMapping("/getAllCandidateService")
    public ResponseEntity<Page<User>> getAllCandidateService(
            @RequestParam int id,
            @RequestParam int page,
            @RequestParam int size) {

        return ResponseEntity.ok(authenticationService.getUserService(id, page, size));
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
        authenticationService.completeUpdated(companyInfoDto.getId());
        ExperienceResponse experienceResponse = new ExperienceResponse();
        experienceResponse.setId(1);
        experienceResponse.setMessage("saved successfully");

        return ResponseEntity.ok(experienceResponse);
    }

    @PostMapping("/updateService")
    public ResponseEntity<ExperienceResponse> updateService(@RequestBody ServiceInfoDto serviceInfoDto) {
        authenticationService.saveServiceInfo(serviceInfoDto);
        authenticationService.completeUpdated(serviceInfoDto.getId());
        ExperienceResponse experienceResponse = new ExperienceResponse();
        experienceResponse.setId(1);
        experienceResponse.setMessage("saved successfully");

        return ResponseEntity.ok(experienceResponse);
    }

    @GetMapping("/getUser")
    public ResponseEntity<UserResponse> getUser(@RequestParam int id) {
        UserResponse user = authenticationService.getUser(id);
        return ResponseEntity.ok(user);
    }
}