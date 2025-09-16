package com.sstinternaltools.sstinternal_tools.policyChatbot.controller;

import com.sstinternaltools.sstinternal_tools.policyChatbot.dtos.ChatResponse;
import com.sstinternaltools.sstinternal_tools.policyChatbot.service.interfaces.ChatService;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/policyChatBot")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/getAns")
    public ChatResponse getAns(@RequestParam String message,@RequestParam @NotNull String conversationId) {
        return chatService.getAns(conversationId,message);
    }
}
