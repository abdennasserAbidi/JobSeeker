package com.myjob.jobseeker.controller;

import com.myjob.jobseeker.dtos.NotificationMessage;
import com.myjob.jobseeker.dtos.UserResponse;
import com.myjob.jobseeker.interfaces.IMessengerService;
import com.myjob.jobseeker.interfaces.INotificationService;
import com.myjob.jobseeker.interfaces.IUserService;
import com.myjob.jobseeker.model.User;
import com.myjob.jobseeker.model.chat.ChatModel;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/auth")
@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class MessengerController {

    private final IMessengerService messengerService;
    private final SimpMessagingTemplate messagingTemplate;
    private final INotificationService notificationService;
    private final IUserService userService;

    @GetMapping("/list_messages")
    public ResponseEntity<Page<ChatModel>> getListMessages(
            @RequestParam int id,
            @RequestParam int page,
            @RequestParam int size
    ) {
        Page<ChatModel> listMessages = messengerService.getListMessages(id, page, size);
        return ResponseEntity.ok(listMessages);
    }

    @GetMapping("/getConversation")
    public ResponseEntity<List<ChatModel>> getConversation(
            @RequestParam int idSender,
            @RequestParam int idReceiver
    ) {
        List<ChatModel> listMessages = messengerService.getConversation(idSender, idReceiver);
        return ResponseEntity.ok(listMessages);
    }

    @MessageMapping("/chat.send")
    public void sendMessage(ChatModel msg, Principal principal) {

        msg.setTimestamp(System.currentTimeMillis());

        // Save to DB
        ChatModel saved = messengerService.saveMessage(msg);

        //notification

        UserResponse userResponse = userService.getUser(saved.getUserReceivedId());

        if (userResponse.getMessage().isEmpty()) {
            User user = userResponse.getUser();
            Map<String, String> data = new HashMap<>();
            data.put("idReceiver", saved.getUserReceivedId() + "");
            data.put("username", saved.getUserReceivedName());
            data.put("idChat", saved.getId() + "");
            data.put("idSender", saved.getUserConnectedId() + "");
            data.put("nameSender", saved.getUserConnectedName());

            NotificationMessage notificationMessage = new NotificationMessage();
            notificationMessage.setRecipientToken(user.getFcmToken());
            notificationMessage.setTitle(saved.getUserConnectedName());
            notificationMessage.setBody("Vous à envoyé un message");
            notificationMessage.setData(data);

            notificationService.sendNotificationChat(notificationMessage);
        }

        // Send to recipient
        messagingTemplate.convertAndSendToUser(
                String.valueOf(msg.getUserReceivedId()),
                "/queue/messages",
                saved
        );
    }

    @MessageMapping("/image.send")
    public void sendImage(ChatModel msg, Principal principal) {

        msg.setTimestamp(System.currentTimeMillis());

        // Save to DB
        ChatModel saved = messengerService.saveMessage(msg);
        // Send to recipient
        messagingTemplate.convertAndSendToUser(
                String.valueOf(msg.getUserReceivedId()),
                "/queue/messages",
                saved
        );
    }
}
