package com.sstinternaltools.sstinternal_tools.documents.service.interfaces;

import com.sstinternaltools.sstinternal_tools.documents.dto.tagDtos.TagCreateDto;
import com.sstinternaltools.sstinternal_tools.documents.dto.tagDtos.TagResponseDto;
import com.sstinternaltools.sstinternal_tools.documents.dto.tagDtos.TagUpdateDto;
import java.util.List;

public interface TagService {
    TagResponseDto createTag(TagCreateDto tagCreateDto);
    TagResponseDto updateTag(Long id, TagUpdateDto tagUpdateDto);
    void deleteTag(Long id);
    List<TagResponseDto> getAllTags();
    TagResponseDto getTagById(Long id);
    List<TagResponseDto> searchTagsByName(String keyword);
}
