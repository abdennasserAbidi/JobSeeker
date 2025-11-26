package com.myjob.jobseeker.services;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.myjob.jobseeker.dtos.NotificationMessage;
import com.myjob.jobseeker.interfaces.INotificationService;
import com.myjob.jobseeker.model.NotificationModel;
import com.myjob.jobseeker.repo.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class NotificationService implements INotificationService {

    private final FirebaseMessaging firebaseMessaging;
    private final UserRepository userRepository;

    public NotificationService(FirebaseMessaging firebaseMessaging, UserRepository userRepository) {
        this.firebaseMessaging = firebaseMessaging;
        this.userRepository = userRepository;
    }

    @Override
    public String sendNotification(NotificationMessage notificationMessage) {

        Message message = Message.builder()
                .setToken(notificationMessage.getRecipientToken())
                .putAllData(notificationMessage.getData())
                .build();

        try {
            firebaseMessaging.send(message);
            return "Success sending notification";
        } catch (Exception e) {
            return "Error sending notification";
        }
    }

    @Override
    public void updateToken(int email, String token) {
        java.util.Optional<com.myjob.jobseeker.model.User> user = userRepository.findById(email);
        user.ifPresent(u -> {
            u.setFcmToken(token);
            userRepository.save(u);
        });
    }

    @Override
    public Page<NotificationModel> getCompanyNotifications(int id, int page, int size) {
        return userRepository.findPaginatedNotification(id, page, size);
    }
    @Override
    public Page<NotificationModel> getPaginatedNotification(int id, int page, int size) {
        return userRepository.findPaginatedNotification(id, page, size);
    }
}