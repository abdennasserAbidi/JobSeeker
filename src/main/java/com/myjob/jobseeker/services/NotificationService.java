package com.myjob.jobseeker.services;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.myjob.jobseeker.dtos.NotificationMessage;
import com.myjob.jobseeker.interfaces.INotificationService;
import com.myjob.jobseeker.model.NotificationModel;
import com.myjob.jobseeker.repo.UserRepository;
import com.myjob.jobseeker.repo.notification.NotifRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.swing.text.View;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class NotificationService implements INotificationService {

    private final FirebaseMessaging firebaseMessaging;
    private final UserRepository userRepository;
    private final NotifRepository notifRepository;

    private static final AtomicInteger idCounter = new AtomicInteger();

    public NotificationService(FirebaseMessaging firebaseMessaging, UserRepository userRepository, NotifRepository notifRepository) {
        this.firebaseMessaging = firebaseMessaging;
        this.userRepository = userRepository;
        this.notifRepository = notifRepository;
    }

    @Override
    public String sendNotification(NotificationMessage notificationMessage, String receiverType) {

        Message message = Message.builder()
                .setToken(notificationMessage.getRecipientToken())
                .setNotification(
                        Notification.builder()
                                .setTitle(notificationMessage.getTitle())
                                .setBody(notificationMessage.getBody())
                                .build()
                )
                .putAllData(notificationMessage.getData())
                .build();

        try {

            firebaseMessaging.send(message);

            NotificationModel notificationModel = new NotificationModel();
            notificationModel.setIdNotification(idCounter.incrementAndGet());

            Map<String, String> data = notificationMessage.getData();

            if (receiverType.equals("company")) {
                notificationModel.setIdCompany(Integer.parseInt(data.get("idReceiver")));
                notificationModel.setIdCandidate(Integer.parseInt(data.get("idCandidate")));
                notificationModel.setIdSender(Integer.parseInt(data.get("idCandidate")));
            } else {
                notificationModel.setIdCompany(Integer.parseInt(data.get("idCompany")));
                notificationModel.setIdCandidate(Integer.parseInt(data.get("idReceiver")));
                notificationModel.setIdSender(Integer.parseInt(data.get("idCompany")));
            }

            notificationModel.setCompanyName(data.get("companyName"));
            notificationModel.setUsername(data.get("username"));

            notificationModel.setIdInvitation(Integer.parseInt(data.get("idInvitation")));
            notificationModel.setIdPost(-1);

            notificationModel.setTitle(notificationMessage.getTitle());
            notificationModel.setDescription(notificationMessage.getBody());
            notificationModel.setRead(false);
            //notificationModel.setDate();

            notifRepository.save(notificationModel);

            return "Success sending notification";
        } catch (Exception e) {
            System.out.println("ftreeeeeeeeee  error  " + e.getMessage());
            return "Error sending notification";
        }
    }

    @Override
    public String sendNotification(NotificationMessage notificationMessage) {

        Message message = Message.builder()
                .setToken(notificationMessage.getRecipientToken())
                .setNotification(
                        Notification.builder()
                                .setTitle(notificationMessage.getTitle())
                                .setBody(notificationMessage.getBody())
                                .build()
                )
                .putAllData(notificationMessage.getData())
                .build();

        try {

            firebaseMessaging.send(message);

            NotificationModel notificationModel = new NotificationModel();
            notificationModel.setIdNotification(idCounter.incrementAndGet());

            Map<String, String> data = notificationMessage.getData();

            notificationModel.setIdCandidate(Integer.parseInt(data.get("idReceiver")));
            notificationModel.setIdCompany(Integer.parseInt(data.get("idCompany")));
            notificationModel.setIdSender(Integer.parseInt(data.get("idCompany")));
            notificationModel.setCompanyName(data.get("companyName"));
            notificationModel.setUsername(data.get("username"));

            notificationModel.setIdPost(Integer.parseInt(data.get("idAnnounce")));
            notificationModel.setIdInvitation(-1);

            notificationModel.setTitle(notificationMessage.getTitle());
            notificationModel.setDescription(notificationMessage.getBody());
            notificationModel.setRead(false);
            //notificationModel.setDate();

            notifRepository.save(notificationModel);

            return "Success sending notification";
        } catch (Exception e) {
            System.out.println("ftreeeeeeeeee  error  " + e.getMessage());
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
    public void seenNotification(int idNotification) {
        NotificationModel notificationModel = notifRepository.findById(idNotification).orElseThrow();
        notificationModel.setRead(true);
        notifRepository.save(notificationModel);
    }

    @Override
    public void removeNotification(int idNotification) {
        NotificationModel notificationModel = notifRepository.findById(idNotification).orElseThrow();
        notifRepository.delete(notificationModel);
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