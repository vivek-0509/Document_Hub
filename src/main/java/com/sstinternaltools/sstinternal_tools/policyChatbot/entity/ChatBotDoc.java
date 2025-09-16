package com.sstinternaltools.sstinternal_tools.policyChatbot.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class ChatBotDoc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String documentName;
    String fileUrl;

    public ChatBotDoc(String documentName,String fileUrl) {
        this.documentName = documentName;
        this.fileUrl = fileUrl;
    }

    public ChatBotDoc(){};

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

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }
}
