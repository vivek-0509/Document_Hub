package com.sstinternaltools.sstinternal_tools.documents.dto.documentDtos;

import com.sstinternaltools.sstinternal_tools.documents.entity.DocumentCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Set;

public class DocumentSummaryDto {
    @NotBlank(message = "Document name cannot be blank")
    private String title;
    @NotNull(message = "Document Category cannot be blank")
    private DocumentCategory category;
    private Set<String> tags;
    @NotBlank(message = "Document file url cannot be blank")
    private String latestFilePath;
    @NotNull(message = "Document upload date cannot be null")
    private LocalDateTime uploadedAt;
    @NotNull(message = "Updates at cannot be blank")
    private LocalDateTime updatedAt;

    public DocumentSummaryDto(String title, DocumentCategory category, Set<String> tags, String latestFilePath, LocalDateTime uploadedAt, LocalDateTime updatedAt) {
        this.title = title;
        this.category = category;
        this.tags = tags;
        this.latestFilePath = latestFilePath;
        this.uploadedAt = uploadedAt;
        this.updatedAt = updatedAt;
    }

    public DocumentSummaryDto() {}

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
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

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public String getLatestFilePath() {
        return latestFilePath;
    }

    public void setLatestFilePath(String latestFilePath) {
        this.latestFilePath = latestFilePath;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
}
