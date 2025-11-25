package com.myjob.jobseeker.controller;

import com.myjob.jobseeker.dtos.ExperienceResponse;
import com.myjob.jobseeker.model.FileExistingResponse;
import com.myjob.jobseeker.model.StoredFile;
import com.myjob.jobseeker.services.FileStorageService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RequestMapping("/auth")
@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class FileController {

    private final FileStorageService fileStorageService;

    @PostMapping("/uploadCV")
    public ResponseEntity<ExperienceResponse> uploadFile(@RequestParam("file") MultipartFile file) {
        ExperienceResponse experienceResponse = new ExperienceResponse();
        experienceResponse.setId(1);

        try {
            // Save the file locally
            System.out.println("file name rzghlrghlzr  "+file.getName());
            String filePath = fileStorageService.storeFile(file);
            file.transferTo(new File(filePath));
            experienceResponse.setMessage(file.getOriginalFilename());
        } catch (Exception e) {
            experienceResponse.setMessage("File upload failed: " + e.getMessage());
        }

        return ResponseEntity.ok(experienceResponse);
    }

    @GetMapping("/isExisted")
    public ResponseEntity<FileExistingResponse> isExisted(@RequestParam String fileName) {
        FileExistingResponse experienceResponse = new FileExistingResponse();
        experienceResponse.setId(1);

        boolean filePath = fileStorageService.isExisted(fileName);
        experienceResponse.setExisted(filePath);

        return ResponseEntity.ok(experienceResponse);
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> retrieveFile(@RequestParam String fileName) {
        return fileStorageService.retrieveFile(fileName);
    }

    @PostMapping("/upload")
    public String uploadFiles(@RequestParam("file") MultipartFile file) throws IOException {
        StoredFile storedFile = new StoredFile();
        storedFile.setFilename(file.getOriginalFilename());
        storedFile.setContentType(file.getContentType());
        storedFile.setData(file.getBytes());
        return "File uploaded successfully!";
    }
}