package com.sstinternaltools.sstinternal_tools.documents.service.interfaces;

import com.sstinternaltools.sstinternal_tools.documents.dto.documentDtos.DocumentCreateDto;
import com.sstinternaltools.sstinternal_tools.documents.dto.documentDtos.DocumentResponseDto;
import com.sstinternaltools.sstinternal_tools.documents.dto.documentDtos.DocumentUpdateDto;
import java.util.List;

public interface DocumentService {
    DocumentResponseDto createDocument(DocumentCreateDto createDto);
    DocumentResponseDto updateDocument(DocumentUpdateDto updateDto, Long documentId);
    void deleteDocument(Long documentId);
    DocumentResponseDto getDocumentById(Long documentId);
    List<DocumentResponseDto> getDocumentByCategoryId(Long categoryId);
    List<DocumentResponseDto> getAllDocuments();
}
