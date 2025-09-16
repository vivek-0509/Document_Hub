package com.sstinternaltools.sstinternal_tools.documents.mapper.implementation;

import com.sstinternaltools.sstinternal_tools.documents.dto.documentDtos.*;
import com.sstinternaltools.sstinternal_tools.documents.entity.Document;
import com.sstinternaltools.sstinternal_tools.documents.entity.DocumentCategory;
import com.sstinternaltools.sstinternal_tools.documents.entity.DocumentVersion;
import com.sstinternaltools.sstinternal_tools.documents.entity.Tag;
import com.sstinternaltools.sstinternal_tools.documents.mapper.interfaces.DocumentDtoMapper;
import com.sstinternaltools.sstinternal_tools.documents.repository.DocumentCategoryRepository;
import com.sstinternaltools.sstinternal_tools.documents.repository.TagRepository;
import com.sstinternaltools.sstinternal_tools.policyChatbot.exception.ResourceNotFoundException;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DocumentDtoMapperImpl implements DocumentDtoMapper {

    private final DocumentCategoryRepository documentCategoryRepository;
    private final TagRepository tagRepository;

    public DocumentDtoMapperImpl(DocumentCategoryRepository documentCategoryRepository, TagRepository tagRepository) {
        this.documentCategoryRepository = documentCategoryRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    public Document toEntity(DocumentCreateDto dto) {

        Set<Tag> tags = dto.getTagId() != null
                ? new HashSet<>(tagRepository.findAllById(dto.getTagId()))
                : null;

        DocumentCategory category = documentCategoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid category ID: " + dto.getCategoryId()));


        Document document = new Document();
        document.setTitle(dto.getTitle());
        document.setCategory(category);
        document.setTags(tags);
        document.setAllowedUsers(dto.getUserAllowed());
        document.setCreatedAt(LocalDateTime.now());
        return document;
    }

    @Override
    public DocumentResponseDto toResponseDto(Document document, DocumentVersion latestVersion) {
        return new DocumentResponseDto(
                document.getId(),
                document.getTitle(),
                document.getCategory(),
                document.getAllowedUsers(),
                document.getTags(),
                latestVersion.getFileUrl(),
                latestVersion.getVersionNumber(),
                document.getCreatedAt(),
                latestVersion.getUploadedByUserEmail(),
                latestVersion.getUploadedAt()
        );
    }

    @Override
    public DocumentSummaryDto toSummaryDto(Document document,DocumentVersion latestVersion) {
        Set<String> tagNames = document.getTags() != null
                ? document.getTags().stream().map(Tag::getName).collect(Collectors.toSet())
                : Set.of();

        return new DocumentSummaryDto(
                document.getTitle(),
                document.getCategory(),
                tagNames,
                latestVersion.getFileUrl(),
                document.getCreatedAt(),
                latestVersion.getUploadedAt()
        );
    }

    @Override
    public Document updateEntity(DocumentUpdateDto dto, Document document) {
        Set<Tag> tags = dto.getTagIds() != null
                ? new HashSet<>(tagRepository.findAllById(dto.getTagIds()))
                : null;

        if (dto.getTitle() != null) {
            document.setTitle(dto.getTitle());
        }
        if (dto.getCategoryId() != null) {
            DocumentCategory category = documentCategoryRepository.findById(dto.getCategoryId()).orElse(null);
            document.setCategory(category);
        }
        if (dto.getAllowedUsers() != null) {
            document.setAllowedUsers(dto.getAllowedUsers());
        }
        if (dto.getTagIds() != null) {
            document.setTags(tags);
        }

        return document;
    }

    @Override
    public DocumentSummaryDto fromResponseToSummaryDto(DocumentResponseDto dto) {
        Set<String> tagNames = dto.getTags() != null
                ? dto.getTags().stream().map(Tag::getName).collect(Collectors.toSet())
                : Set.of();

        return new DocumentSummaryDto(
                dto.getTitle(),
                dto.getCategory(),
                tagNames,
                dto.getLatestFilePath(),
                dto.getUploadedAt(),
                dto.getUpdatedAt()
        );
    }
}
