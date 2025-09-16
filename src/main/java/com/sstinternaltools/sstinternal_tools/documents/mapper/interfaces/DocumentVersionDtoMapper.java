package com.sstinternaltools.sstinternal_tools.documents.mapper.interfaces;

import com.sstinternaltools.sstinternal_tools.documents.dto.documentDtos.DocumentCreateDto;
import com.sstinternaltools.sstinternal_tools.documents.dto.documentVersionDtos.*;
import com.sstinternaltools.sstinternal_tools.documents.entity.DocumentVersion;
import com.sstinternaltools.sstinternal_tools.documents.entity.Document;

public interface DocumentVersionDtoMapper {
    DocumentVersion fromCreateDto(Document document, String fileUrl, String uploadedByUserEmail,Long versionNo);
    DocumentVersionResponseDto toResponseDto(DocumentVersion version);
    DocumentVersionSummaryDto toSummaryDto(DocumentVersion version);
    DocumentVersion fromUpdateDto(DocumentVersionUpdateDto updateDto,DocumentVersion version);
}
