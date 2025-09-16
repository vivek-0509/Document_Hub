package com.sstinternaltools.sstinternal_tools.documents.dto.documentVersionDtos;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public class DocumentVersionCreateDto{
    @NotNull(message="Document Id cannot be null")
    private Long documentId;
    @NotNull(message="Upload the document , it cannot be null")
    private MultipartFile file;

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
