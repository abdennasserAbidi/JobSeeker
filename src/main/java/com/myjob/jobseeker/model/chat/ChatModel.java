package com.myjob.jobseeker.model.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "messages")
public class ChatModel {
    private int id;
    private int userConnectedId;
    private String userConnectedName;
    private int userReceivedId;
    private String userReceivedName;
    private String content;
    private List<String> documents;
    private Long timestamp;
    private MessageType type;
}
