package com.sstinternaltools.sstinternal_tools.documents.service.implementation;

import com.sstinternaltools.sstinternal_tools.documents.dto.tagDtos.TagCreateDto;
import com.sstinternaltools.sstinternal_tools.documents.dto.tagDtos.TagResponseDto;
import com.sstinternaltools.sstinternal_tools.documents.dto.tagDtos.TagUpdateDto;
import com.sstinternaltools.sstinternal_tools.documents.entity.Tag;
import com.sstinternaltools.sstinternal_tools.documents.mapper.interfaces.TagDtoMapper;
import com.sstinternaltools.sstinternal_tools.documents.repository.TagRepository;
import com.sstinternaltools.sstinternal_tools.documents.service.interfaces.TagService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final TagDtoMapper dtoMapper;

    public TagServiceImpl(TagRepository tagRepository, TagDtoMapper dtoMapper) {
        this.tagRepository = tagRepository;
        this.dtoMapper = dtoMapper;
    }

    @Override
    @Transactional
    public TagResponseDto createTag(TagCreateDto tagCreateDto) {
        Tag tag = dtoMapper.fromCreateDto(tagCreateDto);
        tagRepository.save(tag);
        return dtoMapper.toResponseDto(tag);
    }

    @Override
    @Transactional
    public TagResponseDto updateTag(Long id, TagUpdateDto tagUpdateDto) {
        Tag existingTag = tagRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found with id: " + id));
        Tag updatedTag = dtoMapper.fromUpdateDto(tagUpdateDto, existingTag);
        tagRepository.save(updatedTag);
        return dtoMapper.toResponseDto(updatedTag);
    }

    @Override
    @Transactional
    public void deleteTag(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found with id: " + id));
        tagRepository.delete(tag);
    }

    @Override
    public List<TagResponseDto> getAllTags() {
        return tagRepository.findAll()
                .stream()
                .map(dtoMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public TagResponseDto getTagById(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found with id: " + id));
        return dtoMapper.toResponseDto(tag);
    }

    @Override
    public List<TagResponseDto> searchTagsByName(String keyword) {
        return tagRepository.findByNameContainingIgnoreCase(keyword)
                .stream()
                .map(dtoMapper::toResponseDto)
                .collect(Collectors.toList());
    }
}
