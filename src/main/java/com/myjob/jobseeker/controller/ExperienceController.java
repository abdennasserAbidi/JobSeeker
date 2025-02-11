package com.myjob.jobseeker.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.myjob.jobseeker.dtos.ExperienceDto;
import com.myjob.jobseeker.dtos.ExperienceResponse;
import com.myjob.jobseeker.model.Experience;
import com.myjob.jobseeker.services.ExperienceService;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;


@RequestMapping("/exp")
@RestController
@CrossOrigin(origins = "*")  // Allows requests from any domain (or specify your Android IP)
public class ExperienceController {

    private final ExperienceService experienceService;

    public ExperienceController(ExperienceService experienceService) {
        this.experienceService = experienceService;
    }

    @PostMapping("/add")
    public ResponseEntity<ExperienceResponse> register(@RequestBody ExperienceDto experienceDto) {

        System.err.println("grfzgetetexperienceDto   "+experienceDto);

        experienceService.saveExperience(experienceDto);

        ExperienceResponse experienceResponse = new ExperienceResponse();
        experienceResponse.setId(1);
        experienceResponse.setMessage("saved successfully");

        return ResponseEntity.ok(experienceResponse);
    }

    @GetMapping("/getAllExp")
    public ResponseEntity<List<Experience>> getExperiences(@RequestParam int id) {

        List<Experience> experience = experienceService.getAllExperience(id);

        return ResponseEntity.ok(experience);
    }

}
