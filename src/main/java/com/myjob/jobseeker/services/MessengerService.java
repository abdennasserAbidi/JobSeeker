package com.myjob.jobseeker.services;

import com.myjob.jobseeker.interfaces.IMessengerService;
import com.myjob.jobseeker.model.chat.ChatModel;
import com.myjob.jobseeker.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class MessengerService implements IMessengerService {

    private final UserRepository userRepository;

    @Autowired
    public MessengerService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Page<ChatModel> getListMessages(int id, int page, int size) {
        return userRepository.findPaginatedMessages(id, page, size);
    }
}
