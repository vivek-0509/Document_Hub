package com.sstinternaltools.sstinternal_tools.policyChatbot.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public class ChatBotDocCreateDto {

    @NotBlank(message ="Document name cannot be blank")
    String documentName;
    @NotNull(message = "Document cannot be null")
    MultipartFile file;

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
