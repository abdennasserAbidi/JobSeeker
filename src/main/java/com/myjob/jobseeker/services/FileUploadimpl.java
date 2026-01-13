package com.myjob.jobseeker.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.myjob.jobseeker.interfaces.FileUpload;
import com.myjob.jobseeker.model.Documents;
import com.myjob.jobseeker.model.User;
import com.myjob.jobseeker.model.ValidationStatus;
import com.myjob.jobseeker.model.chat.ChatModel;
import com.myjob.jobseeker.repo.UserRepository;
import com.myjob.jobseeker.repo.messenger.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class FileUploadimpl implements FileUpload {

    private final Cloudinary cloudinary;
    private final UserRepository userRepository;
    private final ChatRepository messageRepository;

    @Override
    public Map<String, String> uploadFile(int idUser, MultipartFile multipartFile) throws IOException {

        Map uploadResult = cloudinary.uploader()
                .upload(multipartFile.getBytes(),
                        ObjectUtils.asMap(
                                "resource_type", "image",
                                "folder", multipartFile.getOriginalFilename()
                        )
                );

        String secureUrl = uploadResult.get("secure_url").toString(); // URL with version
        String version = uploadResult.get("version").toString();      // just the version
        String publicId = uploadResult.get("public_id").toString();   // asset ID

        Map<String, String> result = new HashMap<>();
        result.put("secure_url", secureUrl);
        result.put("version", version);
        result.put("public_id", publicId);

        User user = userRepository.findById(idUser).orElseThrow();
        ValidationStatus v = user.getValidationStatus();

        List<String> list = extractNumbers(multipartFile.getOriginalFilename());
        if (!list.isEmpty()) {
            int id = Integer.parseInt(list.get(0));

            List<Documents> documents = v.getDocuments();
            for (Documents doc : documents) {
                if (doc.getId() == id) {
                    doc.setUrl(secureUrl);
                }
            }
            v.setDocuments(documents);
            user.setValidationStatus(v);

            userRepository.save(user);
        }

        return result;
    }

    public Map<String, String> uploadFileChat(int idFrom, int idTo, MultipartFile multipartFile) throws IOException {

        Map uploadResult = cloudinary.uploader()
                .upload(multipartFile.getBytes(),
                        ObjectUtils.asMap(
                                "resource_type", "image",
                                "folder", multipartFile.getOriginalFilename()
                        )
                );

        String secureUrl = uploadResult.get("secure_url").toString(); // URL with version
        String version = uploadResult.get("version").toString();      // just the version
        String publicId = uploadResult.get("public_id").toString();   // asset ID

        Map<String, String> result = new HashMap<>();
        result.put("secure_url", secureUrl);
        result.put("version", version);
        result.put("public_id", publicId);

        List<ChatModel> listChat = messageRepository.findConversation(idFrom, idTo);
        System.out.println("rzjgrhgrhzgzrgzh   idFrom   "+idFrom);
        System.out.println("rzjgrhgrhzgzrgzh   idTo     "+idTo);
        System.out.println("rzjgrhgrhzgzrgzh   "+listChat);
        for (ChatModel chatModel: listChat) {
            List<String> documents = chatModel.getDocuments();
            if (documents != null) {
                documents.add(secureUrl);
                chatModel.setDocuments(documents);
            } else {
                List<String> doc = new ArrayList<>();
                doc.add(secureUrl);
                chatModel.setDocuments(doc);
            }
        }

        messageRepository.saveAll(listChat);

        return result;
    }

    public Map<String, String> uploadFileChatDirect(MultipartFile multipartFile) throws IOException {

        Map uploadResult = cloudinary.uploader()
                .upload(multipartFile.getBytes(),
                        ObjectUtils.asMap(
                                "resource_type", "image",
                                "folder", multipartFile.getOriginalFilename()
                        )
                );

        String secureUrl = uploadResult.get("secure_url").toString(); // URL with version
        String version = uploadResult.get("version").toString();      // just the version
        String publicId = uploadResult.get("public_id").toString();   // asset ID

        Map<String, String> result = new HashMap<>();
        result.put("secure_url", secureUrl);
        result.put("version", version);
        result.put("public_id", publicId);

        return result;
    }

    @Override
    public String getFile(Documents documents) {
        System.out.println("vbfbfgdfdfdfch   "+documents.getId());
        String documentUrl;
        if (documents.getType().equals("pdf")) {
            documentUrl = cloudinary.url()
                    .secure(true)
                    .resourceType("raw")
                    .format("pdf")
                    .generate("document"+documents.getId());
        } else {
            documentUrl = cloudinary.url()
                    .secure(true)
                    .format(documents.getType())
                    .generate("document"+documents.getId());
        }

        return documentUrl;
    }

    List<String> extractNumbers(String input) {
        List<String> numbers = new ArrayList<>();
        Matcher matcher = Pattern.compile("\\d+").matcher(input);
        while (matcher.find()) {
            numbers.add(matcher.group());
        }
        return numbers;
    }
}
