package com.sstinternaltools.sstinternal_tools.documents.service.implementation;

import com.sstinternaltools.sstinternal_tools.documents.dto.documentVersionDtos.DocumentVersionResponseDto;
import com.sstinternaltools.sstinternal_tools.documents.entity.Document;
import com.sstinternaltools.sstinternal_tools.documents.entity.DocumentVersion;
import com.sstinternaltools.sstinternal_tools.documents.mapper.interfaces.DocumentVersionDtoMapper;
import com.sstinternaltools.sstinternal_tools.documents.repository.DocumentVersionRepository;
import com.sstinternaltools.sstinternal_tools.documents.service.interfaces.CloudStorageService;
import com.sstinternaltools.sstinternal_tools.documents.service.interfaces.DocumentVersionService;
import com.sstinternaltools.sstinternal_tools.policyChatbot.exception.ResourceNotFoundException;
import com.sstinternaltools.sstinternal_tools.security.entity.UserPrincipal;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentVersionServiceImpl implements DocumentVersionService {

    private final DocumentVersionDtoMapper documentVersionDtoMapper;
    private final CloudStorageService cloudStorageService;
    private final DocumentVersionRepository documentVersionRepository;

    public DocumentVersionServiceImpl(DocumentVersionDtoMapper documentVersionDtoMapper, CloudStorageService cloudStorageService, DocumentVersionRepository documentVersionRepository) {
        this.documentVersionDtoMapper = documentVersionDtoMapper;
        this.cloudStorageService = cloudStorageService;
        this.documentVersionRepository = documentVersionRepository;
    }

    //Method to create document version
    @Override
    @Transactional
    public void createDocumentVersion(Document document, MultipartFile multipartFile, Long versionNo){
        String email=((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser().getEmail();
        String fileUrl=cloudStorageService.uploadFile(multipartFile);
        DocumentVersion documentVersion=documentVersionDtoMapper.fromCreateDto(document,fileUrl,email,versionNo);
        documentVersionRepository.save(documentVersion);
    }

    @Override
    public List<DocumentVersionResponseDto> getAllVersionsByDocumentId(Long documentId){
       return documentVersionRepository.findByDocumentIdOrderByVersionNumberDesc(documentId).stream().map(documentVersionDtoMapper :: toResponseDto).collect(Collectors.toList());
    }

    @Override
    public DocumentVersionResponseDto getDocumentVersionById(Long versionId) {
        DocumentVersion version = documentVersionRepository.findById(versionId)
                .orElseThrow(() -> new ResourceNotFoundException("Document version not found with ID: " + versionId));

        return documentVersionDtoMapper.toResponseDto(version);
    }

    @Override
    @Transactional
    public void deleteDocumentVersion(Long versionId) {
        DocumentVersion version = documentVersionRepository.findById(versionId)
                .orElseThrow(() -> new ResourceNotFoundException("Document version not found with ID: " + versionId));
        cloudStorageService.deleteFile(version.getFileUrl());
        documentVersionRepository.delete(version);
    }
}
