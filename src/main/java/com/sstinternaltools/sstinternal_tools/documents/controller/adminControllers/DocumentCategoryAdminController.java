package com.sstinternaltools.sstinternal_tools.documents.controller.adminControllers;

import com.sstinternaltools.sstinternal_tools.documents.dto.documentCategoryDto.DocumentCategoryCreateDto;
import com.sstinternaltools.sstinternal_tools.documents.dto.documentCategoryDto.DocumentCategoryResponseDto;
import com.sstinternaltools.sstinternal_tools.documents.dto.documentCategoryDto.DocumentCategoryUpdateDto;
import com.sstinternaltools.sstinternal_tools.documents.entity.DocumentCategory;
import com.sstinternaltools.sstinternal_tools.documents.mapper.interfaces.DocumentCategoryDtoMapper;
import com.sstinternaltools.sstinternal_tools.documents.service.interfaces.DocumentCategoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/document/admin/category")
public class DocumentCategoryAdminController {

    private final DocumentCategoryService documentCategoryService;
    private final DocumentCategoryDtoMapper documentCategoryDtoMapper;

    public DocumentCategoryAdminController(DocumentCategoryService documentCategoryService, DocumentCategoryDtoMapper documentCategoryDtoMapper) {
        this.documentCategoryService = documentCategoryService;
        this.documentCategoryDtoMapper = documentCategoryDtoMapper;
    }

    // Create new category
    @PostMapping("/create")
    public ResponseEntity<DocumentCategoryResponseDto> createCategory(
            @RequestBody @Valid DocumentCategoryCreateDto dto) {
        DocumentCategoryResponseDto created = documentCategoryService.createDocumentCategory(dto);
        return ResponseEntity.ok(created);
    }

    // Update category
    @PutMapping("/update/{id}")
    public ResponseEntity<DocumentCategoryResponseDto> updateCategory(
            @PathVariable Long id,
            @RequestBody @Valid DocumentCategoryUpdateDto dto) {
        DocumentCategoryResponseDto updated = documentCategoryService.updateCategory(id, dto);
        return ResponseEntity.ok(updated);
    }

    // Delete category
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        documentCategoryService.deleteCategory(id);
        return ResponseEntity.ok("Document category deleted successfully");
    }

    // Get category by ID
    @GetMapping("getById/{id}")
    public ResponseEntity<DocumentCategoryResponseDto> getCategoryById(@PathVariable Long id) {
        DocumentCategory category = documentCategoryService.getCategoryById(id);
        DocumentCategoryResponseDto responseDto=documentCategoryDtoMapper.toResponseDto(category);
        return ResponseEntity.ok(responseDto);
    }

    // Get all categories
    @GetMapping("getAll")
    public ResponseEntity<List<DocumentCategoryResponseDto>> getAllCategories() {
        List<DocumentCategoryResponseDto> categories = documentCategoryService.getAllCategories().stream().map(documentCategoryDtoMapper::toResponseDto).collect(Collectors.toList());
        return ResponseEntity.ok(categories);
    }

    // Search categories by normalized name
    @GetMapping("/search")
    public ResponseEntity<List<DocumentCategoryResponseDto>> searchCategories(@RequestParam String keyword) {
        List<DocumentCategoryResponseDto> results = documentCategoryService.searchByNormalizedName(keyword).stream().map(documentCategoryDtoMapper::toResponseDto).collect(Collectors.toList());
        return ResponseEntity.ok(results);
    }
}
