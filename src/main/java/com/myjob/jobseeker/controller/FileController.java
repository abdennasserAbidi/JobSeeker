package com.myjob.jobseeker.controller;

import com.myjob.jobseeker.dtos.ExperienceResponse;
import com.myjob.jobseeker.dtos.UserResponse;
import com.myjob.jobseeker.interfaces.FileUpload;
import com.myjob.jobseeker.interfaces.IMessengerService;
import com.myjob.jobseeker.model.*;
import com.myjob.jobseeker.model.chat.ChatModel;
import com.myjob.jobseeker.model.chat.MessageType;
import com.myjob.jobseeker.services.AuthService;
import com.myjob.jobseeker.services.FileStorageService;
import lombok.AllArgsConstructor;
import org.cloudinary.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    private final IMessengerService messengerService;
    private final SimpMessagingTemplate messagingTemplate;

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
    public ResponseEntity<?> uploadImage(
            @RequestParam("idUser") int idUser,
            @RequestParam("image") MultipartFile multipartFile
    ) {
        try {
            Map<String, String> result = fileUpload.uploadFile(idUser, multipartFile);

            String imageURL = result.get("secure_url");
            String version = result.get("version");
            String publicId = result.get("version");

            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("imageURL", result.get("secure_url"));
            return ResponseEntity.ok(jsonResponse.toString());
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Une erreur s'est produite lors du téléchargement de l'image.");
        }
    }

    @PostMapping("/uploadChat")
    public ResponseEntity<?> uploadImageChat(
            @RequestParam("idFrom") int idFrom,
            @RequestParam("idTo") int idTo,
            @RequestParam("image") MultipartFile multipartFile
    ) {
        try {
            Map<String, String> result = fileUpload.uploadFileChat(idFrom, idTo, multipartFile);

            String imageURL = result.get("secure_url");
            String version = result.get("version");
            String publicId = result.get("version");

            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("imageURL", imageURL);
            return ResponseEntity.ok(jsonResponse.toString());
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Une erreur s'est produite lors du téléchargement de l'image.");
        }
    }

    @PostMapping(value = "/uploadDirect", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadDirect(
            @RequestParam("idFrom") int idFrom,
            @RequestParam("idTo") int idTo,
            @RequestPart("image") List<MultipartFile> multipartFiles
    ) {
        try {
            List<String> listDoc = new ArrayList<>();
            ChatModel chatModel = new ChatModel();

            for (MultipartFile multipartFile: multipartFiles) {
                Map<String, String> result = fileUpload.uploadFileChatDirect(multipartFile);

                String imageURL = result.get("secure_url");
                String version = result.get("version");
                String publicId = result.get("version");


                listDoc.add(imageURL);

            }

            User userFrom = authService.getUser(idFrom).getUser();
            String roleFrom = userFrom.getRole();
            if (roleFrom.equals("Candidat") || roleFrom.equals("Candidate")) {
                chatModel.setUserConnectedName(userFrom.getFullName());
            } else chatModel.setUserConnectedName(userFrom.getCompanyName());

            User userTo = authService.getUser(idTo).getUser();
            String roleTo = userFrom.getRole();
            if (roleTo.equals("Candidat") || roleTo.equals("Candidate")) {
                chatModel.setUserReceivedName(userTo.getFullName());
            } else chatModel.setUserReceivedName(userTo.getCompanyName());

            chatModel.setId(4353053);
            chatModel.setUserConnectedId(idFrom);
            chatModel.setUserReceivedId(idTo);
            chatModel.setContent("");
            chatModel.setContent("");
            chatModel.setContent("");
            chatModel.setType(MessageType.CHAT);
            chatModel.setDocuments(listDoc);
            chatModel.setTimestamp(System.currentTimeMillis());

            ChatModel saved = messengerService.saveMessage(chatModel);
            System.out.println("fezghrgkzgr    "+saved);
            // Send to recipient
            messagingTemplate.convertAndSendToUser(
                    String.valueOf(idTo),
                    "/queue/messages",
                    saved
            );

            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("imageURL", listDoc);
            return ResponseEntity.ok(jsonResponse.toString());
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Une erreur s'est produite lors du téléchargement de l'image.");
        }
    }

    @GetMapping("/getFiles")
    public ResponseEntity<?> getFiles(@RequestParam("id") int id) {
        try {
            List<String> list = new ArrayList<>();
            UserResponse user = authService.getUser(id);
            List<Documents> documents = user.getUser().getValidationStatus().getDocuments();
            for (Documents doc : documents) {
                //list.add(fileUpload.getFile(doc));
                list.add(doc.getUrl());
            }

            FileResponse fileResponse = new FileResponse();
            fileResponse.setUrl(list);

            return ResponseEntity.ok(fileResponse);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Une erreur s'est produite lors du téléchargement de l'image.");
        }
    }
}