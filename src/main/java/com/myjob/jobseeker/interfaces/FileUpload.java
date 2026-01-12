package com.myjob.jobseeker.interfaces;

import com.myjob.jobseeker.model.Documents;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface FileUpload {
    Map<String, String> uploadFile(int idUser, MultipartFile multipartFile) throws IOException;
    Map<String, String> uploadFileChat(int idFrom, int idTo, MultipartFile multipartFile) throws IOException;
    String getFile(Documents documents);
}
