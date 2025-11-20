package com.myjob.jobseeker.model.chat;

import lombok.Data;

import java.util.List;

@Data
public class Conversation {
    private String id;
    private String name;
    private List<String> participants;
    private String lastMessage;
    private Long lastMessageTime;
}
