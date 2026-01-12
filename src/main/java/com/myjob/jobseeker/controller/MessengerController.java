package com.myjob.jobseeker.controller;

import com.myjob.jobseeker.interfaces.IMessengerService;
import com.myjob.jobseeker.model.chat.ChatModel;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequestMapping("/auth")
@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class MessengerController {

    private final IMessengerService messengerService;
    private final SimpMessagingTemplate messagingTemplate;

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
        System.out.println("rzjgrhgrhzgzrgzh   saved     "+saved);
        // Send to recipient
        messagingTemplate.convertAndSendToUser(
                String.valueOf(msg.getUserReceivedId()),
                "/queue/messages",
                saved
        );
    }
}
