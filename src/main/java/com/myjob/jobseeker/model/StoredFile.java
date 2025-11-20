package com.myjob.jobseeker.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class StoredFile {
    @Id
    private String id;
    private String filename;
    private String contentType;
    private byte[] data; // For smaller files
}
