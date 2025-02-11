package com.myjob.jobseeker.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    public FileStorageService(@Value("${file.upload-dir}") String uploadDir) {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException ex) {
            throw new RuntimeException("Could not create the directory where uploaded files will be stored.", ex);
        }
    }

    public String storeFile(MultipartFile file) {
        try {
            // Generate the file's destination path
            Path targetLocation = this.fileStorageLocation.resolve(file.getOriginalFilename());
            if (Files.exists(targetLocation)) {
                System.err.println("fjenafkeanfea   is already exist");
            } else {
                // Copy the file to the destination
            Files.copy(file.getInputStream(), targetLocation);
            }
            
            return targetLocation.toString();
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + file.getOriginalFilename() + ". Please try again!", ex);
        }
    }

    public ResponseEntity<Resource> retrieveFile(String fileName) {
        try {
            // Generate the file's destination path
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            if (!Files.exists(targetLocation)) {
                return ResponseEntity.notFound().build();
            }
            
            Resource resource = new UrlResource(targetLocation.toUri());

            // Set the response headers
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(resource);
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public boolean isExisted(String fileName) {
        Path targetLocation = this.fileStorageLocation.resolve(fileName);
        return Files.exists(targetLocation);
    }
}
