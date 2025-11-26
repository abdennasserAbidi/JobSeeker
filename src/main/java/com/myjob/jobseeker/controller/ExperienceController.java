package com.myjob.jobseeker.controller;

import com.myjob.jobseeker.dtos.ExperienceDto;
import com.myjob.jobseeker.dtos.ExperienceResponse;
import com.myjob.jobseeker.interfaces.IExperienceService;
import com.myjob.jobseeker.model.Experience;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/auth")
@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class ExperienceController {

    private final IExperienceService experienceService;


    @PostMapping("/add")
    public ResponseEntity<ExperienceResponse> register(@RequestBody ExperienceDto experienceDto) {

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

    @GetMapping("/getAllExperience")
    public ResponseEntity<Page<Experience>> getExperiencesPages(
            @RequestParam int id,
            @RequestParam int page,
            @RequestParam int size) {

        Page<Experience> experiences = experienceService.getPaginatedExperiences(id, page, size);

        return ResponseEntity.ok(experiences);
    }

    @PostMapping("/removeExperience")
    public ResponseEntity<ExperienceResponse> removeExperience(@RequestParam int id, @RequestParam int experienceId) {
        experienceService.removeExperience(id, experienceId);

        ExperienceResponse experienceResponse = new ExperienceResponse();
        experienceResponse.setId(1);
        experienceResponse.setMessage("removed successfully");

        return ResponseEntity.ok(experienceResponse);
    }
}