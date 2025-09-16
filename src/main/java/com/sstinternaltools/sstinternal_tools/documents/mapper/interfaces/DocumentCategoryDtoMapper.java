package com.sstinternaltools.sstinternal_tools.documents.mapper.interfaces;

import com.sstinternaltools.sstinternal_tools.documents.dto.documentCategoryDto.*;
import com.sstinternaltools.sstinternal_tools.documents.entity.DocumentCategory;

public interface DocumentCategoryDtoMapper {

    DocumentCategory toEntity(DocumentCategoryCreateDto dto,String normalizedName);

    DocumentCategoryResponseDto toResponseDto(DocumentCategory category);

    DocumentCategorySummaryDto toSummaryDto(DocumentCategory category);

    void updateEntity(DocumentCategoryUpdateDto dto, DocumentCategory category);
}
