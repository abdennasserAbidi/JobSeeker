package com.myjob.jobseeker.services;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.myjob.jobseeker.dtos.NotificationMessage;
import com.myjob.jobseeker.interfaces.INotificationService;
import com.myjob.jobseeker.model.NotificationModel;
import com.myjob.jobseeker.repo.UserRepository;
import com.myjob.jobseeker.repo.notification.NotifRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class NotificationService implements INotificationService {

    private final FirebaseMessaging firebaseMessaging;
    private final UserRepository userRepository;
    private final NotifRepository notifRepository;

    public NotificationService(FirebaseMessaging firebaseMessaging, UserRepository userRepository, NotifRepository notifRepository) {
        this.firebaseMessaging = firebaseMessaging;
        this.userRepository = userRepository;
        this.notifRepository = notifRepository;
    }

    @Override
    public String sendNotification(NotificationMessage notificationMessage) {

        Message message = Message.builder()
                .setToken(notificationMessage.getRecipientToken())
                .putAllData(notificationMessage.getData())
                .build();

        try {
            firebaseMessaging.send(message);
            Map<String, String> data = notificationMessage.getData();
            for (Map.Entry<String, String> entry : data.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                System.out.println("Key: " + key + ", Value: " + value);
                System.out.println("ftreeeeeeeeee  error  "+value);
            }
            /*NotificationModel notificationModel = new NotificationModel();
            notificationModel.setIdNotification();
            notificationModel.setCompanyName();
            notificationModel.setIdNotification();

            notifRepository.save(notificationModel);*/

            return "Success sending notification";
        } catch (Exception e) {
            System.out.println("ftreeeeeeeeee  error  "+e.getMessage());
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
        return notifRepository.findPaginatedNotification(id, page, size);
    }
    @Override
    public Page<NotificationModel> getPaginatedNotification(int id, int page, int size) {
        return notifRepository.findPaginatedNotification(id, page, size);
    }
}