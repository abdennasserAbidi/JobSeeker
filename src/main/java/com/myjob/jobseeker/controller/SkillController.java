package com.myjob.jobseeker.controller;

import com.myjob.jobseeker.services.SkillExtractorService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/auth")
@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class SkillController {

    private final SkillExtractorService skillExtractorService;


    @PostMapping("/extract")
    public Map<String, List<String>> extract(@RequestParam String description) {
        return skillExtractorService.extract(description);
    }
}