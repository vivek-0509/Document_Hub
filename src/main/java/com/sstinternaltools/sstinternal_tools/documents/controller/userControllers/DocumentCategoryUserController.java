package com.sstinternaltools.sstinternal_tools.documents.controller.userControllers;

import com.sstinternaltools.sstinternal_tools.documents.dto.documentCategoryDto.DocumentCategorySummaryDto;
import com.sstinternaltools.sstinternal_tools.documents.entity.DocumentCategory;
import com.sstinternaltools.sstinternal_tools.documents.mapper.interfaces.DocumentCategoryDtoMapper;
import com.sstinternaltools.sstinternal_tools.documents.service.interfaces.DocumentCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/document/user/category")
public class DocumentCategoryUserController {

    private final DocumentCategoryService documentCategoryService;
    private final DocumentCategoryDtoMapper documentCategoryDtoMapper;

    public DocumentCategoryUserController(DocumentCategoryService documentCategoryService, DocumentCategoryDtoMapper documentCategoryDtoMapper) {
        this.documentCategoryService = documentCategoryService;
        this.documentCategoryDtoMapper = documentCategoryDtoMapper;
    }

    // Get category by ID
    @GetMapping("getById/{id}")
    public ResponseEntity<DocumentCategorySummaryDto> getCategoryById(@PathVariable Long id) {
        DocumentCategory category = documentCategoryService.getCategoryById(id);
        DocumentCategorySummaryDto summaryDto=documentCategoryDtoMapper.toSummaryDto(category);
        return ResponseEntity.ok(summaryDto);
    }

    // Get all categories
    @GetMapping("getAll")
    public ResponseEntity<List<DocumentCategorySummaryDto>> getAllCategories() {
        List<DocumentCategorySummaryDto> categories = documentCategoryService.getAllCategories().stream().map(documentCategoryDtoMapper::toSummaryDto).collect(Collectors.toList());
        return ResponseEntity.ok(categories);
    }

    // Search categories by normalized name
    @GetMapping("/search")
    public ResponseEntity<List<DocumentCategorySummaryDto>> searchCategories(@RequestParam String keyword) {
        List<DocumentCategorySummaryDto> results = documentCategoryService.searchByNormalizedName(keyword).stream().map(documentCategoryDtoMapper::toSummaryDto).collect(Collectors.toList());
        return ResponseEntity.ok(results);
    }
}

