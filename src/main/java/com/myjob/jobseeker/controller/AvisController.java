package com.myjob.jobseeker.controller;

import lombok.AllArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.myjob.jobseeker.dtos.ExperienceResponse;
import com.myjob.jobseeker.interfaces.IAvisService;
import com.myjob.jobseeker.model.Avis;

@RequestMapping("/auth")
@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class AvisController {

    private IAvisService iAvisService;

    ///////////////////////////////////////////////////////////////////////////
    // ADD
    ///////////////////////////////////////////////////////////////////////////
    @PostMapping("/saveAvis")
    public ResponseEntity<ExperienceResponse> addAvis(@RequestBody Avis avis) {

        iAvisService.changeAvis(avis);
        
        ExperienceResponse experienceResponse = new ExperienceResponse();
        experienceResponse.setId(1);
        experienceResponse.setMessage("saved successfully");

        return ResponseEntity.ok(experienceResponse);
    }

    @GetMapping("/getCandidateAvis")
    public ResponseEntity<Page<Avis>> getCandidateAvis(
            @RequestParam int idCandidate,
            @RequestParam int page,
            @RequestParam int size) {


        System.out.println("gjrkgnjkrgrzg    "+idCandidate);        

        Page<Avis> avisList = iAvisService.getUserAvis(idCandidate, page, size);

        return ResponseEntity.ok(avisList);
    }



}
