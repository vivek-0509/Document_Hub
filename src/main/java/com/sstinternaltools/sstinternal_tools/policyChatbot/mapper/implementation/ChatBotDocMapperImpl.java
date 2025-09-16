package com.sstinternaltools.sstinternal_tools.policyChatbot.mapper.implementation;

import com.sstinternaltools.sstinternal_tools.policyChatbot.dtos.ChatBotDocCreateDto;
import com.sstinternaltools.sstinternal_tools.policyChatbot.dtos.ChatBotDocResponseDto;
import com.sstinternaltools.sstinternal_tools.policyChatbot.entity.ChatBotDoc;
import com.sstinternaltools.sstinternal_tools.policyChatbot.mapper.interfaces.ChatBotDocMapper;
import org.springframework.stereotype.Component;

@Component
public class ChatBotDocMapperImpl implements ChatBotDocMapper {

    @Override
    public ChatBotDoc fromCreateDto(ChatBotDocCreateDto createDto,String fileUrl) {
        ChatBotDoc chatBotDoc = new ChatBotDoc();
        chatBotDoc.setDocumentName(createDto.getDocumentName());
        chatBotDoc.setFileUrl(fileUrl);
        return chatBotDoc;
    }

    @Override
    public ChatBotDocResponseDto fromEntityToDto(ChatBotDoc chatBotDoc) {
        ChatBotDocResponseDto responseDto=new ChatBotDocResponseDto();
        responseDto.setDocumentName(chatBotDoc.getDocumentName());
        responseDto.setFileUrl(chatBotDoc.getFileUrl());
        responseDto.setId(chatBotDoc.getId());
        return responseDto;
    }
}
