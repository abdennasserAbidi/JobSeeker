package com.myjob.jobseeker.interfaces;

import com.myjob.jobseeker.model.chat.ChatModel;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IMessengerService {
    Page<ChatModel> getListMessages(int id, int page, int size);
    List<ChatModel> getConversation(int idSender, int idReceiver);
    ChatModel saveMessage(ChatModel msg);
}
