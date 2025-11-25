package com.myjob.jobseeker.controller;

import com.myjob.jobseeker.dtos.*;
import com.myjob.jobseeker.model.SearchHistory;
import com.myjob.jobseeker.model.User;
import com.myjob.jobseeker.services.AuthenticationService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
@RestController
@CrossOrigin(origins = "*")
public class SearchController {

    private final AuthenticationService authenticationService;

    public SearchController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

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
}