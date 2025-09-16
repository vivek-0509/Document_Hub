package com.sstinternaltools.sstinternal_tools.policyChatbot.mapper.interfaces;

import com.sstinternaltools.sstinternal_tools.policyChatbot.dtos.ChatBotDocCreateDto;
import com.sstinternaltools.sstinternal_tools.policyChatbot.dtos.ChatBotDocResponseDto;
import com.sstinternaltools.sstinternal_tools.policyChatbot.entity.ChatBotDoc;

public interface ChatBotDocMapper {

     ChatBotDoc fromCreateDto(ChatBotDocCreateDto createDto, String fileUrl);
     ChatBotDocResponseDto fromEntityToDto(ChatBotDoc chatBotDoc);
}
