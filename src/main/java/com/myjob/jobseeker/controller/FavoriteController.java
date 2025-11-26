package com.myjob.jobseeker.controller;

import com.myjob.jobseeker.dtos.ExperienceResponse;
import com.myjob.jobseeker.interfaces.IFavoriteService;
import com.myjob.jobseeker.model.FavoriteModel;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class FavoriteController {

    private final IFavoriteService favoriteService;


    @PostMapping("/updatefavorite")
    public ResponseEntity<ExperienceResponse> updateFavorite(@RequestParam int idUserConnected, @RequestParam int candidateId) {

        favoriteService.updateFavorite(idUserConnected, candidateId);

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

        Page<FavoriteModel> experiences = favoriteService.getPaginatedFavorites(id, page, size);

        return ResponseEntity.ok(experiences);
    }
}