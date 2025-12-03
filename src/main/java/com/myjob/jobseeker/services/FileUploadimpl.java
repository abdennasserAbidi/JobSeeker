package com.myjob.jobseeker.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.myjob.jobseeker.interfaces.FileUpload;
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
        //Map.of("public_id", UUID.randomUUID().toString())
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
}
