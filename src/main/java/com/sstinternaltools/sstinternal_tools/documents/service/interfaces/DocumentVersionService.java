package com.sstinternaltools.sstinternal_tools.documents.service.interfaces;

import com.sstinternaltools.sstinternal_tools.documents.dto.documentVersionDtos.DocumentVersionResponseDto;
import com.sstinternaltools.sstinternal_tools.documents.entity.Document;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DocumentVersionService {
    void createDocumentVersion(Document document, MultipartFile multipartFile, Long versionNo);
    List<DocumentVersionResponseDto> getAllVersionsByDocumentId(Long documentId);
    DocumentVersionResponseDto getDocumentVersionById(Long versionId);
    void deleteDocumentVersion(Long versionId);
}
