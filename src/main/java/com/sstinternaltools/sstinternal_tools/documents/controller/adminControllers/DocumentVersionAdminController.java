package com.sstinternaltools.sstinternal_tools.documents.controller.adminControllers;

import com.sstinternaltools.sstinternal_tools.documents.service.interfaces.DocumentVersionService;
import org.springframework.http.ResponseEntity;
import com.sstinternaltools.sstinternal_tools.documents.dto.documentVersionDtos.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/document/admin/version/")
public class DocumentVersionAdminController {

    private final DocumentVersionService documentVersionService;

    public DocumentVersionAdminController(DocumentVersionService documentVersionService) {
        this.documentVersionService = documentVersionService;
    }


    @GetMapping("getAllByDocId/{documentId}")
    public ResponseEntity<List<DocumentVersionResponseDto>> getAllVersions(@PathVariable Long documentId) {
        List<DocumentVersionResponseDto> versions = documentVersionService.getAllVersionsByDocumentId(documentId);
        return ResponseEntity.ok(versions);
    }

    @GetMapping("getByVersionId/{versionId}")
    public ResponseEntity<DocumentVersionResponseDto> getVersionById(@PathVariable Long versionId) {
        DocumentVersionResponseDto version = documentVersionService.getDocumentVersionById(versionId);
        return ResponseEntity.ok(version);
    }

    @DeleteMapping("/delete/{versionId}")
    public ResponseEntity<String> deleteVersion(@PathVariable Long versionId) {
        documentVersionService.deleteDocumentVersion(versionId);
        return ResponseEntity.ok("Document version deleted successfully.");
    }
}