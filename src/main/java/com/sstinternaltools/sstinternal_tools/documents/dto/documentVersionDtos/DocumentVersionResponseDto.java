package com.sstinternaltools.sstinternal_tools.documents.dto.documentVersionDtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class DocumentVersionResponseDto {
    @NotNull(message = "Version Id name cannot be null")
    private Long id;
    @NotBlank(message = "Document name cannot be null")
    private String documentName;
    @NotNull(message = "Document version cannot be null")
    private Long versionNumber;
    @NotBlank(message = "Version file url cannot be null")
    private String fileUrl;
    @NotNull(message = "Upload Time cannot be null")
    private LocalDateTime uploadedAt ;
    @NotBlank(message = "Author Email cannot be null")
    private String uploadedByEmail;
    @NotNull(message="Boolean for latest version cannot be null")
    private Boolean isLatestVersion;

    public DocumentVersionResponseDto(Long id, String documentName, Long versionNumber, String fileUrl, LocalDateTime uploadedAt, String uploadedByEmail , Boolean isLatestVersion) {
        this.id = id;
        this.documentName = documentName;
        this.versionNumber = versionNumber;
        this.fileUrl = fileUrl;
        this.uploadedAt = uploadedAt;
        this.uploadedByEmail = uploadedByEmail;
        this.isLatestVersion = isLatestVersion;
    }

    public DocumentVersionResponseDto() {}

    public Boolean getLatestVersion() {
        return isLatestVersion;
    }

    public void setLatestVersion(Boolean latestVersion) {
        isLatestVersion = latestVersion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getUploadedByEmail() {
        return uploadedByEmail;
    }

    public void setUploadedByEmail(String uploadedByEmail) {
        this.uploadedByEmail = uploadedByEmail;
    }
}
