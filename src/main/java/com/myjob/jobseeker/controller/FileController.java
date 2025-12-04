package com.myjob.jobseeker.controller;

import com.myjob.jobseeker.dtos.ExperienceResponse;
import com.myjob.jobseeker.interfaces.FileUpload;
import com.myjob.jobseeker.model.*;
import com.myjob.jobseeker.services.AuthService;
import com.myjob.jobseeker.services.FileStorageService;
import lombok.AllArgsConstructor;
import org.cloudinary.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/auth")
@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class FileController {

    private final FileStorageService fileStorageService;

    @Autowired
    private final AuthService authService;

    @Autowired
    private final FileUpload fileUpload;

    @PostMapping("/uploadCV")
    public ResponseEntity<ExperienceResponse> uploadFileCV(@RequestParam("file") MultipartFile file) {
        ExperienceResponse experienceResponse = new ExperienceResponse();
        experienceResponse.setId(1);

        try {
            // Save the file locally
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

    @PostMapping("/uploadFiles")
    public String uploadFiles(@RequestParam("file") MultipartFile file) throws IOException {
        StoredFile storedFile = new StoredFile();
        storedFile.setFilename(file.getOriginalFilename());
        storedFile.setContentType(file.getContentType());
        storedFile.setData(file.getBytes());
        return "File uploaded successfully!";
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile multipartFile) {
        try {
            String imageURL = fileUpload.uploadFile(multipartFile);
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("imageURL", imageURL);
            return ResponseEntity.ok(jsonResponse.toString());
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Une erreur s'est produite lors du téléchargement de l'image.");
        }
    }

    @GetMapping("/getFiles")
    public ResponseEntity<?> getFiles(@RequestParam("id") int id) {
        try {
            List<String> list = new ArrayList<>();
            User user = authService.getUser(id);
            List<Documents> documents = user.getValidationStatus().getDocuments();
            for (Documents doc : documents) {
                list.add(fileUpload.getFile(doc));
            }

            FileResponse fileResponse = new FileResponse();
            fileResponse.setUrl(list);

            return ResponseEntity.ok(fileResponse);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Une erreur s'est produite lors du téléchargement de l'image.");
        }
    }
}