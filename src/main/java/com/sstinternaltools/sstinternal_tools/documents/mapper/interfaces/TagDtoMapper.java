package com.sstinternaltools.sstinternal_tools.documents.mapper.interfaces;

import com.sstinternaltools.sstinternal_tools.documents.dto.tagDtos.TagCreateDto;
import com.sstinternaltools.sstinternal_tools.documents.dto.tagDtos.TagResponseDto;
import com.sstinternaltools.sstinternal_tools.documents.dto.tagDtos.TagSummaryDto;
import com.sstinternaltools.sstinternal_tools.documents.dto.tagDtos.TagUpdateDto;
import com.sstinternaltools.sstinternal_tools.documents.entity.Tag;

public interface TagDtoMapper {
    Tag fromCreateDto(TagCreateDto createDto);
    Tag fromUpdateDto(TagUpdateDto updateDto, Tag entity);
    TagResponseDto toResponseDto(Tag tag);
    TagSummaryDto toSummaryDto(Tag tag);
}
