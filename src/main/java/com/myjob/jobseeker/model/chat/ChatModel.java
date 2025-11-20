package com.myjob.jobseeker.model.chat;

import lombok.Data;

@Data
public class ChatModel {
    private String id;
    private String conversationId;
    private String senderId;
    private String senderName;
    private String content;
    private Long timestamp;
    private MessageType type;
}
