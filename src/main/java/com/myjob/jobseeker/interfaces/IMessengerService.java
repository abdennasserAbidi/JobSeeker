package com.myjob.jobseeker.interfaces;

import com.myjob.jobseeker.model.chat.ChatModel;
import org.springframework.data.domain.Page;

public interface IMessengerService {
    Page<ChatModel> getListMessages(int id, int page, int size);
}
