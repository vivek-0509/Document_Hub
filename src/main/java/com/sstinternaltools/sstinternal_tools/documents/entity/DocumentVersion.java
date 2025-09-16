package com.sstinternaltools.sstinternal_tools.documents.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class DocumentVersion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "document_id", nullable = false)
    private Document document;

    private Long versionNumber;

    private String fileUrl; // Cloud URL or file path

    private LocalDateTime uploadedAt ;

    private Boolean isLatestVersion;

    private String uploadedByUserEmail;

    public DocumentVersion(Document document,Long versionNumber, String fileUrl,String uploadedByUserEmail,Boolean isLatestVersion) {
        this.document = document;
        this.versionNumber = versionNumber;
        this.fileUrl = fileUrl;
        this.uploadedByUserEmail = uploadedByUserEmail;
        this.isLatestVersion = isLatestVersion;
    }

    public DocumentVersion() {}

    public Boolean isLatestVersion() {
        return isLatestVersion;
    }

    public void setLatestVersion(Boolean latestVersion) {
        isLatestVersion = latestVersion;
    }

    public Long getId() {
        return id;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public Long getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(Long versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public String getUploadedByUserEmail() {
        return uploadedByUserEmail;
    }

    public void setUploadedByUserEmail(String uploadedByUserEmail) {
        this.uploadedByUserEmail = uploadedByUserEmail;
    }
}