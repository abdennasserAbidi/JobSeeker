package com.myjob.jobseeker.repo.messenger;

import com.myjob.jobseeker.model.chat.ChatModel;
import org.springframework.data.domain.Page;

public interface ChatRepository {
    Page<ChatModel> findPaginatedMessages(int id, int page, int size);
}
