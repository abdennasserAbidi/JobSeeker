package com.myjob.jobseeker.interfaces;

import com.myjob.jobseeker.model.Documents;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileUpload {
    String uploadFile(MultipartFile multipartFile) throws IOException;
    String getFile(Documents documents);
}
