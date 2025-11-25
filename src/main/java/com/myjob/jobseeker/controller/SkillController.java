package com.myjob.jobseeker.controller;

import com.myjob.jobseeker.services.SkillExtractorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/auth")
@RestController
@CrossOrigin(origins = "*")
public class SkillController {

    private final SkillExtractorService skillExtractorService;

    public SkillController(SkillExtractorService skillExtractorService) {
        this.skillExtractorService = skillExtractorService;
    }

    @PostMapping("/extract")
    public Map<String, List<String>> extract(@RequestParam String description) {
        return skillExtractorService.extract(description);
    }
}