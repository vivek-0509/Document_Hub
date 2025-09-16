package com.sstinternaltools.sstinternal_tools.documents.mapper.implementation;

import com.sstinternaltools.sstinternal_tools.documents.dto.documentVersionDtos.*;
import com.sstinternaltools.sstinternal_tools.documents.entity.Document;
import com.sstinternaltools.sstinternal_tools.documents.entity.DocumentVersion;
import com.sstinternaltools.sstinternal_tools.documents.mapper.interfaces.DocumentVersionDtoMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DocumentVersionDtoMapperImpl implements DocumentVersionDtoMapper {
    @Override
    public DocumentVersion fromCreateDto(Document document, String fileUrl, String uploadedByUserEmail,Long versionNo) {
        DocumentVersion version = new DocumentVersion(document,versionNo, fileUrl, uploadedByUserEmail,true);
        version.setUploadedAt(LocalDateTime.now());
        return version;
    }

    @Override
    public DocumentVersionResponseDto toResponseDto(DocumentVersion version) {
        DocumentVersionResponseDto dto = new DocumentVersionResponseDto();
        dto.setId(version.getId());
        dto.setDocumentName(version.getDocument().getTitle()+" V"+version.getVersionNumber());
        dto.setUploadedAt(version.getUploadedAt());
        dto.setFileUrl(version.getFileUrl());
        dto.setVersionNumber(version.getVersionNumber());
        dto.setLatestVersion(version.isLatestVersion());
        dto.setUploadedByEmail(version.getUploadedByUserEmail());
        return dto;
    }

    @Override
    public DocumentVersionSummaryDto toSummaryDto(DocumentVersion version) {
        return new DocumentVersionSummaryDto(
                version.getDocument().getTitle(),
                version.getVersionNumber(),
                version.getFileUrl(),
                version.getUploadedAt()
        );
    }

    @Override
    public DocumentVersion fromUpdateDto(DocumentVersionUpdateDto dto,DocumentVersion version) {
        version.setUploadedAt(LocalDateTime.now());
        version.setLatestVersion(version.isLatestVersion());
        return version;
    }

}
