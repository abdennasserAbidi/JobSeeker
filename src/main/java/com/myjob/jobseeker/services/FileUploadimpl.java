package com.myjob.jobseeker.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.myjob.jobseeker.interfaces.FileUpload;
import com.myjob.jobseeker.model.Documents;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

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
                                "public_id", UUID.randomUUID().toString()
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
                    .resourceType("raw")
                    .format("pdf")
                    .generate("myfile"); // myfile = public_id
        } else {
            documentUrl = cloudinary.url()
                    .format(documents.getType())
                    .generate("sample"); // sample = public_id
        }

        return documentUrl;
    }
}
