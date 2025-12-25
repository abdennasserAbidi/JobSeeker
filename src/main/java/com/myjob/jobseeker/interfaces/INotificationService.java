package com.myjob.jobseeker.interfaces;

import com.myjob.jobseeker.dtos.NotificationMessage;
import com.myjob.jobseeker.model.NotificationModel;
import org.springframework.data.domain.Page;

public interface INotificationService {
    String sendNotification(NotificationMessage notificationMessage, String receiverType);
    String sendNotification(NotificationMessage notificationMessage);
    void updateToken(int id, String token);
    void seenNotification(int idNotification);
    void removeNotification(int idNotification);
    Page<NotificationModel> getCompanyNotifications(int id, int page, int size);

    Page<NotificationModel> getPaginatedNotification(int id, int page, int size);
}
