package com.sstinternaltools.sstinternal_tools.documents.service.interfaces;

import com.sstinternaltools.sstinternal_tools.documents.dto.documentCategoryDto.DocumentCategoryCreateDto;
import com.sstinternaltools.sstinternal_tools.documents.dto.documentCategoryDto.DocumentCategoryResponseDto;
import com.sstinternaltools.sstinternal_tools.documents.dto.documentCategoryDto.DocumentCategoryUpdateDto;
import com.sstinternaltools.sstinternal_tools.documents.entity.DocumentCategory;

import java.util.List;

public interface DocumentCategoryService {
    DocumentCategoryResponseDto createDocumentCategory(DocumentCategoryCreateDto dto);
    DocumentCategoryResponseDto updateCategory(Long id, DocumentCategoryUpdateDto dto);
    void deleteCategory(Long id);
    DocumentCategory getCategoryById(Long id);
    List<DocumentCategory> getAllCategories();
    List<DocumentCategory> searchByNormalizedName(String keyword);
}
