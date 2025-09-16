package com.sstinternaltools.sstinternal_tools.policyChatbot.service.interfaces;

import com.sstinternaltools.sstinternal_tools.policyChatbot.dtos.ChatBotDocCreateDto;
import com.sstinternaltools.sstinternal_tools.policyChatbot.dtos.ChatBotDocResponseDto;

import java.util.List;

public interface ChatBotDocService {
     void injectDocument(ChatBotDocCreateDto createDto);
     void deleteDocumentAndEmbeddings(Long docId);
     List<ChatBotDocResponseDto> getAllDocuments();
}
