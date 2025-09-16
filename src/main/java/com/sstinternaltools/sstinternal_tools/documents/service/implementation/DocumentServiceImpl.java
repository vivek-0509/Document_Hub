package com.sstinternaltools.sstinternal_tools.documents.service.implementation;

import com.sstinternaltools.sstinternal_tools.documents.dto.documentDtos.DocumentCreateDto;
import com.sstinternaltools.sstinternal_tools.documents.dto.documentDtos.DocumentResponseDto;
import com.sstinternaltools.sstinternal_tools.documents.dto.documentDtos.DocumentUpdateDto;
import com.sstinternaltools.sstinternal_tools.documents.entity.AllowedUsers;
import com.sstinternaltools.sstinternal_tools.documents.entity.Document;
import com.sstinternaltools.sstinternal_tools.documents.entity.DocumentVersion;
import com.sstinternaltools.sstinternal_tools.documents.mapper.interfaces.DocumentDtoMapper;
import com.sstinternaltools.sstinternal_tools.documents.repository.DocumentRepository;
import com.sstinternaltools.sstinternal_tools.documents.repository.DocumentVersionRepository;
import com.sstinternaltools.sstinternal_tools.documents.service.interfaces.DocumentService;
import com.sstinternaltools.sstinternal_tools.documents.service.interfaces.DocumentVersionService;
import com.sstinternaltools.sstinternal_tools.policyChatbot.exception.ResourceNotFoundException;
import com.sstinternaltools.sstinternal_tools.security.entity.UserPrincipal;
import com.sstinternaltools.sstinternal_tools.security.exception.InvalidCredentialsException;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final DocumentVersionRepository documentVersionRepository;
    private final DocumentDtoMapper documentDtoMapper;
    private final DocumentVersionService documentVersionService;

    public DocumentServiceImpl(DocumentRepository documentRepository, DocumentVersionRepository documentVersionRepository, DocumentDtoMapper documentDtoMapper,DocumentVersionService documentVersionService) {
        this.documentRepository = documentRepository;
        this.documentVersionRepository = documentVersionRepository;
        this.documentDtoMapper = documentDtoMapper;
        this.documentVersionService = documentVersionService;
    }

    @Override
    @Transactional
    public DocumentResponseDto createDocument(DocumentCreateDto createDto){
        Document document=documentDtoMapper.toEntity(createDto);
        documentRepository.save(document);
        documentVersionService.createDocumentVersion(document,createDto.getFile(),1L);
        return getDocumentById(document.getId());
    }

    @Override
    @Transactional
    public DocumentResponseDto updateDocument(DocumentUpdateDto updateDto,Long documentId){
        Document document=documentRepository.findById(documentId).orElseThrow(()-> new ResourceNotFoundException("Document not found"));
        Document updatedDocument=documentDtoMapper.updateEntity(updateDto,document);

        if(updateDto.getFile()!=null && !updateDto.getFile().isEmpty()){
            DocumentVersion currentDocument=documentVersionRepository.findByDocumentIdAndIsLatestVersionTrue(documentId);
            currentDocument.setLatestVersion(false);
            Long versionNo=documentVersionRepository.findTopByDocumentIdOrderByVersionNumberDesc(documentId).getVersionNumber();
            documentVersionService.createDocumentVersion(updatedDocument,updateDto.getFile(),versionNo+1);
        }
        documentRepository.save(updatedDocument);
        return getDocumentById(updatedDocument.getId());
    }

    @Override
    @Transactional
    public void deleteDocument(Long documentId){
        documentVersionRepository.findByDocumentIdOrderByVersionNumberDesc(documentId).stream().map(DocumentVersion::getId)
                .forEach(documentVersionService:: deleteDocumentVersion);
        documentVersionRepository.deleteAllByDocumentId(documentId);
        documentRepository.deleteById(documentId);
    }

    @Override
    public DocumentResponseDto getDocumentById(Long documentId){
        // Step 1: Get authentication
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Set<AllowedUsers> userBatchRoles=getUserBatchRole(authentication);
        String email = authentication.getName(); // typically the email

        // Step 4: Fetch document
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));

        Set<AllowedUsers> documentAllowedUsers = document.getAllowedUsers(); // e.g., [ADMIN, BATCH2024]
        documentAllowedUsers.add(AllowedUsers.ADMIN);
        documentAllowedUsers.add(AllowedUsers.SUPER_ADMIN);

        boolean isAllowed = documentAllowedUsers.contains(AllowedUsers.ALL) || userBatchRoles.stream().anyMatch(documentAllowedUsers::contains);

        if (!isAllowed) {
            throw new InvalidCredentialsException("You are not authorized to view this document.");
        }
        DocumentVersion latestVersion = documentVersionRepository.findByDocumentIdAndIsLatestVersionTrue(documentId);
        return documentDtoMapper.toResponseDto(document, latestVersion);
    }


    // Step 2: Extract user roles from JWT (SecurityContext)
    public Set<AllowedUsers> getUserBatchRole(Authentication authentication) {
        Set<AllowedUsers> userAllowedRoles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)       // e.g., ROLE_STUDENT
                .map(role -> role.replace("ROLE_", ""))    // → STUDENT
                .map(role -> {
                    try {
                        return AllowedUsers.valueOf(role); // AllowedUsers.STUDENT
                    } catch (IllegalArgumentException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());


        String email=((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser().getEmail();

        try {
            String batch = extractBatchYear(email); // e.g., "24"
            userAllowedRoles.add(AllowedUsers.valueOf("BATCH20" + batch));
        } catch (IllegalArgumentException e) {
            // skip invalid batch year
        }

        return userAllowedRoles;
    };

    //Method to extract batch from the student email
    public String extractBatchYear(String email) {
        String localPart = email.split("@")[0]; // "vivek.24bcs10338"
        String[] parts = localPart.split("\\."); // ["vivek", "24bcs10338"]
        return parts[1].substring(0, 2); // "24"
    }

    @Override
    public List<DocumentResponseDto> getDocumentByCategoryId(Long categoryId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new InvalidCredentialsException("User is not authenticated.");
        }
        // Extract user roles from SecurityContext
        Set<AllowedUsers> userBatchRoles =getUserBatchRole(authentication);
        // Fetch all documents by category
        List<Document> documents = documentRepository.findByCategoryId(categoryId);
        // Filter and map only the allowed ones
        List<DocumentResponseDto> allowedDocuments = documents.stream()
                .filter(doc -> {
                    Set<AllowedUsers> docAllowedUsers = doc.getAllowedUsers();
                    docAllowedUsers.add(AllowedUsers.ADMIN);
                    docAllowedUsers.add(AllowedUsers.SUPER_ADMIN);
                    return docAllowedUsers.contains(AllowedUsers.ALL) ||
                            userBatchRoles.stream().anyMatch(docAllowedUsers::contains);
                })
                .map(doc -> {
                    DocumentVersion latestVersion = documentVersionRepository
                            .findByDocumentIdAndIsLatestVersionTrue(doc.getId());
                    return documentDtoMapper.toResponseDto(doc, latestVersion);
                })
                .collect(Collectors.toList());

        return allowedDocuments;
    }

    @Override
    public List<DocumentResponseDto> getAllDocuments() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new InvalidCredentialsException("User is not authenticated.");
        }
        // Extract user roles from SecurityContext
        Set<AllowedUsers> userBatchRoles =getUserBatchRole(authentication);
        // Fetch all documents by category
        List<Document> documents = documentRepository.findAll();
        // Filter and map only the allowed ones
        List<DocumentResponseDto> allowedDocuments = documents.stream()
                .filter(doc -> {
                    Set<AllowedUsers> docAllowedUsers = doc.getAllowedUsers();
                    docAllowedUsers.add(AllowedUsers.ADMIN);
                    docAllowedUsers.add(AllowedUsers.SUPER_ADMIN);
                    return docAllowedUsers.contains(AllowedUsers.ALL) ||
                            userBatchRoles.stream().anyMatch(docAllowedUsers::contains);
                })
                .map(doc -> {
                    DocumentVersion latestVersion = documentVersionRepository
                            .findByDocumentIdAndIsLatestVersionTrue(doc.getId());
                    return documentDtoMapper.toResponseDto(doc, latestVersion);
                })
                .collect(Collectors.toList());

        return allowedDocuments;
    }

}
