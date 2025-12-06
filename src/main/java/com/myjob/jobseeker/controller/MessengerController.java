package com.myjob.jobseeker.controller;

import com.myjob.jobseeker.interfaces.IMessengerService;
import com.myjob.jobseeker.model.chat.ChatModel;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/auth")
@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class MessengerController {

    private final IMessengerService messengerService;

    @PostMapping("/list_messages")
    public ResponseEntity<Page<ChatModel>> getListMessages(
            @RequestParam int id,
            @RequestParam int page,
            @RequestParam int size
    ) {
        Page<ChatModel> listMessages = messengerService.getListMessages(id, page, size);
        return ResponseEntity.ok(listMessages);
    }
}
