package com.myjob.jobseeker.repo.messenger;

import com.myjob.jobseeker.model.chat.ChatModel;
import org.springframework.data.domain.Page;

public interface MessageRepository {
    Page<ChatModel> findPaginatedMessages(int id, int page, int size);
}
