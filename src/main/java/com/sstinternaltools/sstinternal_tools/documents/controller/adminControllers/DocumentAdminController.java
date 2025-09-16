package com.sstinternaltools.sstinternal_tools.documents.controller.adminControllers;

import com.sstinternaltools.sstinternal_tools.documents.dto.documentDtos.DocumentCreateDto;
import com.sstinternaltools.sstinternal_tools.documents.dto.documentDtos.DocumentResponseDto;
import com.sstinternaltools.sstinternal_tools.documents.dto.documentDtos.DocumentUpdateDto;
import com.sstinternaltools.sstinternal_tools.documents.service.interfaces.DocumentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/document/admin")
public class DocumentAdminController {

    private final DocumentService documentService;

    public DocumentAdminController(DocumentService documentService) {
        this.documentService = documentService;
    }

    //  Create a new document
    @PostMapping("/create")
    public ResponseEntity<DocumentResponseDto> createDocument(
            @ModelAttribute @Valid DocumentCreateDto createDto) {
        DocumentResponseDto responseDto = documentService.createDocument(createDto);
        return ResponseEntity.ok(responseDto);
    }

    // Update an existing document
    @PutMapping("update/{id}")
    public ResponseEntity<DocumentResponseDto> updateDocument(
            @PathVariable Long id,
            @ModelAttribute @Valid DocumentUpdateDto updateDto) {
        DocumentResponseDto responseDto = documentService.updateDocument(updateDto, id);
        return ResponseEntity.ok(responseDto);
    }

    // Delete document by ID
    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteDocument(@PathVariable Long id) {
        documentService.deleteDocument(id);
        return ResponseEntity.ok("Document deleted successfully");
    }

    // Get document by ID (with access check)
    @GetMapping("getById/{id}")
    public ResponseEntity<DocumentResponseDto> getDocumentById(@PathVariable Long id) {
        DocumentResponseDto responseDto = documentService.getDocumentById(id);
        return ResponseEntity.ok(responseDto);
    }

    //Get documents by category (with filtering by access)
    @GetMapping("/byCategory/{categoryId}")
    public ResponseEntity<List<DocumentResponseDto>> getDocumentsByCategoryId(@PathVariable Long categoryId) {
        List<DocumentResponseDto> results = documentService.getDocumentByCategoryId(categoryId);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<DocumentResponseDto>> getAllDocuments() {
        List<DocumentResponseDto> results = documentService.getAllDocuments();
        return ResponseEntity.ok(results);
    }
}