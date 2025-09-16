package com.sstinternaltools.sstinternal_tools.documents.dto.documentVersionDtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class DocumentVersionSummaryDto{

    @NotBlank(message = "Document name cannot be null")
    private String documentName;
    @NotNull(message = "Document version cannot be null")
    private Long versionNumber;
    @NotBlank(message = "Version file url cannot be null")
    private String fileUrl;
    @NotNull(message = "Upload Time cannot be null")
    private LocalDateTime uploadedAt ;

    public DocumentVersionSummaryDto(String documentName, Long versionNumber, String fileUrl, LocalDateTime uploadedAt) {
        this.documentName = documentName;
        this.versionNumber = versionNumber;
        this.fileUrl = fileUrl;
        this.uploadedAt = uploadedAt;
    }

    public DocumentVersionSummaryDto() {}

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
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
}
