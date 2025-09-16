package com.sstinternaltools.sstinternal_tools.documents.mapper.interfaces;

import com.sstinternaltools.sstinternal_tools.documents.dto.documentDtos.DocumentCreateDto;
import com.sstinternaltools.sstinternal_tools.documents.dto.documentDtos.DocumentResponseDto;
import com.sstinternaltools.sstinternal_tools.documents.dto.documentDtos.DocumentSummaryDto;
import com.sstinternaltools.sstinternal_tools.documents.dto.documentDtos.DocumentUpdateDto;
import com.sstinternaltools.sstinternal_tools.documents.entity.Document;
import com.sstinternaltools.sstinternal_tools.documents.entity.DocumentVersion;

public interface DocumentDtoMapper {

    Document toEntity(DocumentCreateDto dto);
    DocumentResponseDto toResponseDto(Document document, DocumentVersion latestVersion);
    DocumentSummaryDto toSummaryDto(Document document, DocumentVersion latestVersion);
    Document updateEntity(DocumentUpdateDto dto, Document document);
    DocumentSummaryDto fromResponseToSummaryDto(DocumentResponseDto dto);
}
