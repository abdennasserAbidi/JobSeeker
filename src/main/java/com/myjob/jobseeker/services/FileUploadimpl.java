package com.myjob.jobseeker.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.myjob.jobseeker.interfaces.FileUpload;
import com.myjob.jobseeker.model.Documents;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class FileUploadimpl implements FileUpload {

    private final Cloudinary cloudinary;

    @Override
    public String uploadFile(MultipartFile multipartFile) throws IOException {
        return cloudinary.uploader()
                .upload(multipartFile.getBytes(),
                        ObjectUtils.asMap(
                                "resource_type", "raw",
                                "public_id", multipartFile.getOriginalFilename()
                        )
                )
                .get("url")
                .toString();
    }

    @Override
    public String getFile(Documents documents) {
        String documentUrl;
        if (documents.getType().equals("pdf")) {
            documentUrl = cloudinary.url()
                    .secure(true)
                    .resourceType("raw")
                    .format("pdf")
                    .generate("document "+documents.getId());
        } else {
            documentUrl = cloudinary.url()
                    .secure(true)
                    .format(documents.getType())
                    .generate("document "+documents.getId());
        }

        return documentUrl;
    }
}
