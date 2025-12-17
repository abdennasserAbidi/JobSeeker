package com.myjob.jobseeker.services;

import com.myjob.jobseeker.dtos.Criteria;
import com.myjob.jobseeker.interfaces.IMessengerService;
import com.myjob.jobseeker.model.User;
import com.myjob.jobseeker.model.chat.ChatModel;
import com.myjob.jobseeker.repo.messenger.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class MessengerService implements IMessengerService {

    private final ChatRepository chatRepository;

    @Autowired
    public MessengerService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Override
    public Page<ChatModel> getListMessages(int id, int page, int size) {
        return chatRepository.findPaginatedAllMessages(id, page, size);
    }

    @Override
    public List<ChatModel> getConversation(int idSender, int idReceiver) {
        return chatRepository.findConversation(idSender, idReceiver);
    }

    @Override
    public ChatModel saveMessage(ChatModel msg) {
        return chatRepository.save(msg);
    }
}
