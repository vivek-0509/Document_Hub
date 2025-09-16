package com.sstinternaltools.sstinternal_tools.documents.controller.userControllers;

import com.sstinternaltools.sstinternal_tools.documents.dto.documentDtos.DocumentResponseDto;
import com.sstinternaltools.sstinternal_tools.documents.dto.documentDtos.DocumentSummaryDto;
import com.sstinternaltools.sstinternal_tools.documents.mapper.interfaces.DocumentDtoMapper;
import com.sstinternaltools.sstinternal_tools.documents.service.interfaces.DocumentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/document/user")
public class DocumentUserController {

    private final DocumentService documentService;
    private final DocumentDtoMapper documentDtoMapper;

    public DocumentUserController(DocumentService documentService, DocumentDtoMapper documentDtoMapper) {
        this.documentService = documentService;
        this.documentDtoMapper = documentDtoMapper;
    }

    // Get document by ID (with access check)
    @GetMapping("getById/{id}")
    public ResponseEntity<DocumentSummaryDto> getDocumentById(@PathVariable Long id) {
        DocumentResponseDto responseDto = documentService.getDocumentById(id);
        DocumentSummaryDto summaryDto=documentDtoMapper.fromResponseToSummaryDto(responseDto);
        return ResponseEntity.ok(summaryDto);
    }

    // Get documents by category (with filtering by access)
    @GetMapping("/byCategory/{categoryId}")
    public ResponseEntity<List<DocumentSummaryDto>> getDocumentsByCategoryId(@PathVariable Long categoryId) {
        List<DocumentSummaryDto> results = documentService.getDocumentByCategoryId(categoryId).stream().map(documentDtoMapper::fromResponseToSummaryDto).collect(Collectors.toList());
        return ResponseEntity.ok(results);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<DocumentSummaryDto>> getAllDocuments() {
        List<DocumentSummaryDto> results = documentService.getAllDocuments().stream().map(documentDtoMapper::fromResponseToSummaryDto).collect(Collectors.toList());
        return ResponseEntity.ok(results);
    }
}
