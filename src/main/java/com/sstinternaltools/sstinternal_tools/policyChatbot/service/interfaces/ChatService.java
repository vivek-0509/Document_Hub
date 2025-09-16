package com.sstinternaltools.sstinternal_tools.policyChatbot.service.interfaces;

import com.sstinternaltools.sstinternal_tools.policyChatbot.dtos.ChatResponse;

public interface ChatService {
    ChatResponse getAns(String conversationId,String message);
}
