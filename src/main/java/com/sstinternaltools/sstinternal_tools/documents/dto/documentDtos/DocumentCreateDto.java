package com.sstinternaltools.sstinternal_tools.documents.dto.documentDtos;

import com.sstinternaltools.sstinternal_tools.documents.entity.AllowedUsers;
import com.sstinternaltools.sstinternal_tools.documents.entity.DocumentCategory;
import com.sstinternaltools.sstinternal_tools.documents.entity.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;
import java.util.Set;

public class DocumentCreateDto {
    @NotBlank(message = "Document name cannot be blank")
    private String title;
    @NotNull(message = "Document category cannot be blank")
    private Long categoryId;
    private Set<Long> tagId;
    @NotNull(message = "Allowed users cannot be null")
    private Set<AllowedUsers> userAllowed;
    @NotNull(message = "File cannot be null")
    private MultipartFile file;

    public @NotBlank(message = "Document name cannot be blank") String getTitle() {
        return title;
    }

    public void setTitle(@NotBlank(message = "Document name cannot be blank") String title) {
        this.title = title;
    }

    public @NotNull(message = "Document category cannot be blank") Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(@NotNull(message = "Document category cannot be blank") Long categoryId) {
        this.categoryId = categoryId;
    }

    public Set<Long> getTagId() {
        return tagId;
    }

    public void setTagId(Set<Long> tagId) {
        this.tagId = tagId;
    }

    public @NotNull(message = "Allowed users cannot be null") Set<AllowedUsers> getUserAllowed() {
        return userAllowed;
    }

    public void setUserAllowed(@NotNull(message = "Allowed users cannot be null") Set<AllowedUsers> userAllowed) {
        this.userAllowed = userAllowed;
    }

    public @NotNull(message = "File cannot be null") MultipartFile getFile() {
        return file;
    }

    public void setFile(@NotNull(message = "File cannot be null") MultipartFile file) {
        this.file = file;
    }
}
