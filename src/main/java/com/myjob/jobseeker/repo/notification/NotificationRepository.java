package com.myjob.jobseeker.repo.notification;

import com.myjob.jobseeker.model.NotificationModel;
import org.springframework.data.domain.Page;

public interface NotificationRepository {
    Page<NotificationModel> findPaginatedNotification(int id, int page, int size);
}
