package com.sstinternaltools.sstinternal_tools.policyChatbot.controller;

import com.sstinternaltools.sstinternal_tools.policyChatbot.dtos.ChatBotDocCreateDto;
import com.sstinternaltools.sstinternal_tools.policyChatbot.dtos.ChatBotDocResponseDto;
import com.sstinternaltools.sstinternal_tools.policyChatbot.service.interfaces.ChatBotDocService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("policyChatBot/admin")
public class PolicyChatBotAdminController {

    private final ChatBotDocService chatBotDocService;

    public PolicyChatBotAdminController(ChatBotDocService chatBotDocService) {
        this.chatBotDocService = chatBotDocService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@ModelAttribute @Valid ChatBotDocCreateDto chatBotDocCreateDto) {
        chatBotDocService.injectDocument(chatBotDocCreateDto);
        return ResponseEntity.ok("Document uploaded successfully");
    }

    @DeleteMapping("/delete/{Id}")
    public ResponseEntity<String> deleteFile(@PathVariable Long Id){
        chatBotDocService.deleteDocumentAndEmbeddings(Id);
        return ResponseEntity.ok("Document deleted successfully");
    }

    @GetMapping("/getAllDocs")
    public ResponseEntity<List<ChatBotDocResponseDto>> getAllDocuments(){
       List<ChatBotDocResponseDto> allDocs=chatBotDocService.getAllDocuments();
       return ResponseEntity.ok(allDocs);
    }

}
