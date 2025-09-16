package com.sstinternaltools.sstinternal_tools.documents.service.implementation;

import com.sstinternaltools.sstinternal_tools.documents.dto.documentCategoryDto.DocumentCategoryCreateDto;
import com.sstinternaltools.sstinternal_tools.documents.dto.documentCategoryDto.DocumentCategoryResponseDto;
import com.sstinternaltools.sstinternal_tools.documents.dto.documentCategoryDto.DocumentCategoryUpdateDto;
import com.sstinternaltools.sstinternal_tools.documents.entity.DocumentCategory;
import com.sstinternaltools.sstinternal_tools.documents.exception.DuplicateCategoryException;
import com.sstinternaltools.sstinternal_tools.documents.mapper.interfaces.DocumentCategoryDtoMapper;
import com.sstinternaltools.sstinternal_tools.documents.repository.DocumentCategoryRepository;
import com.sstinternaltools.sstinternal_tools.documents.repository.DocumentRepository;
import com.sstinternaltools.sstinternal_tools.documents.service.interfaces.DocumentCategoryService;
import com.sstinternaltools.sstinternal_tools.documents.service.interfaces.LemmatizerService;
import com.sstinternaltools.sstinternal_tools.policyChatbot.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentCategoryServiceImpl implements DocumentCategoryService {

    private final DocumentCategoryRepository documentCategoryRepository;
    private final DocumentCategoryDtoMapper documentCategoryDtoMapper;
    private final LemmatizerService lemmatizerService;
    private final DocumentRepository documentRepository;

    public DocumentCategoryServiceImpl(DocumentCategoryRepository documentCategoryRepository, DocumentCategoryDtoMapper documentCategoryDtoMapper,LemmatizerService lemmatizerService,DocumentRepository documentRepository) {
        this.documentCategoryRepository = documentCategoryRepository;
        this.documentCategoryDtoMapper = documentCategoryDtoMapper;
        this.lemmatizerService = lemmatizerService;
        this.documentRepository=documentRepository;
    }

    @Override
    @Transactional
    public DocumentCategoryResponseDto createDocumentCategory(DocumentCategoryCreateDto dto) {
        String rawName = dto.getName();
        String normalized = lemmatizerService.lemmatize(rawName);
        boolean alreadyExists = documentCategoryRepository.existsByNormalizedName(normalized);
        if (alreadyExists) {
            throw new DuplicateCategoryException("Category already exists with similar name: " + rawName);
        }
        DocumentCategory category = documentCategoryDtoMapper.toEntity(dto,normalized);
        documentCategoryRepository.save(category);
        return documentCategoryDtoMapper.toResponseDto(category);
    }

    @Override
    @Transactional
    public DocumentCategoryResponseDto updateCategory(Long id ,DocumentCategoryUpdateDto dto) {
        DocumentCategory category = documentCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));

        String normalized = lemmatizerService.lemmatize(dto.getName());
        // Check if another category with same normalized name exists
        boolean exists = documentCategoryRepository.existsByNormalizedNameAndIdNot(normalized, id);
        if (exists) {
            throw new DuplicateCategoryException("Another category already exists with similar name: " + dto.getName());
        }
        category.setName(dto.getName());
        category.setNormalizedName(normalized);
        documentCategoryRepository.save(category);
        return documentCategoryDtoMapper.toResponseDto(category);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        if (!documentCategoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category not found with ID: " + id);
        }

        if (documentRepository.existsByCategoryId(id)) {
            throw new RuntimeException("Cannot delete category because documents are associated with it.");
        }

        documentCategoryRepository.deleteById(id);
    }

    @Override
    public DocumentCategory getCategoryById(Long id) {
        DocumentCategory category = documentCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + id));
        return category;
    }

    @Override
    public List<DocumentCategory> getAllCategories() {
        List<DocumentCategory> categories = documentCategoryRepository.findAll();
        return categories;
    }

    @Override
    public List<DocumentCategory> searchByNormalizedName(String keyword) {
        String normalized = lemmatizerService.lemmatize(keyword);  // optional
        return documentCategoryRepository.findByNormalizedNameContainingIgnoreCase(normalized);
    }

}
