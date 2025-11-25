package com.myjob.jobseeker.controller;

import com.myjob.jobseeker.dtos.EducationDto;
import com.myjob.jobseeker.dtos.ExperienceResponse;
import com.myjob.jobseeker.model.Education;
import com.myjob.jobseeker.services.AuthenticationService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/auth")
@RestController
@CrossOrigin(origins = "*")
public class EducationController {

    private final AuthenticationService authenticationService;

    public EducationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
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
}