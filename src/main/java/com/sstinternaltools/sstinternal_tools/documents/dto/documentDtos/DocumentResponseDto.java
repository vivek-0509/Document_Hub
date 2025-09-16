package com.sstinternaltools.sstinternal_tools.documents.dto.documentDtos;

import com.sstinternaltools.sstinternal_tools.documents.entity.AllowedUsers;
import com.sstinternaltools.sstinternal_tools.documents.entity.DocumentCategory;
import com.sstinternaltools.sstinternal_tools.documents.entity.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Set;

public class DocumentResponseDto{
    @NotNull(message = "Document id cannot be null")
    private Long id;
    @NotBlank(message = "Document name cannot be blank")
    private String title;
    @NotNull(message = "Document category cannot be null")
    private DocumentCategory category;
    @NotNull(message = "Allowed Users cannot be null")
    private Set<AllowedUsers> allowedUsers;
    private Set<Tag> tags;
    @NotBlank(message = "Document file URL cannot be blank")
    private String latestFilePath;
    @NotNull(message = "Document Version cannot be null")
    private Long versionNumber;
    @NotNull(message = "Uploading time cannot be null")
    private LocalDateTime uploadedAt;
    @NotBlank(message = "Uploaded by field cannot be blank")
    private String uploadedBy;
    @NotNull(message = "Updates at cannot be blank")
    private LocalDateTime updatedAt;

    public DocumentResponseDto(Long id, String title, DocumentCategory category, Set<AllowedUsers> allowedUsers, Set<Tag> tags, String latestFilePath, Long versionNumber, LocalDateTime uploadedAt, String uploadedBy, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.allowedUsers = allowedUsers;
        this.tags = tags;
        this.latestFilePath = latestFilePath;
        this.versionNumber = versionNumber;
        this.uploadedAt = uploadedAt;
        this.uploadedBy = uploadedBy;
        this.updatedAt = updatedAt;
    }

    public DocumentResponseDto() {}

    public Long getId() {
        return id;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt( LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public DocumentCategory getCategory() {
        return category;
    }

    public void setCategory(DocumentCategory category) {
        this.category = category;
    }

    public Set<AllowedUsers> getAllowedUsers() {
        return allowedUsers;
    }

    public void setAllowedUsers(Set<AllowedUsers> allowedUsers) {
        this.allowedUsers = allowedUsers;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public String getLatestFilePath() {
        return latestFilePath;
    }

    public void setLatestFilePath(String latestFilePath) {
        this.latestFilePath = latestFilePath;
    }

    public Long getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(Long versionNumber) {
        this.versionNumber = versionNumber;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }
}
