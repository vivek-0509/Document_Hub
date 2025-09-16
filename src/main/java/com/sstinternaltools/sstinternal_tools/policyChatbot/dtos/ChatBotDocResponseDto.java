package com.sstinternaltools.sstinternal_tools.policyChatbot.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public class ChatBotDocResponseDto {

    @NotNull(message = "Document Id cannot be null")
    Long id;
    @NotBlank(message ="Document name cannot be blank")
    String documentName;
    @NotNull(message = "Document cannot be null")
    String fileUrl;

    public ChatBotDocResponseDto(String fileUrl, String documentName, Long id) {
        this.fileUrl = fileUrl;
        this.documentName = documentName;
        this.id = id;
    }

    public ChatBotDocResponseDto(){};

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
