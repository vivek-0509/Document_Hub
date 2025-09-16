package com.sstinternaltools.sstinternal_tools.documents.mapper.implementation;

import com.sstinternaltools.sstinternal_tools.documents.dto.tagDtos.*;
import com.sstinternaltools.sstinternal_tools.documents.entity.Tag;
import com.sstinternaltools.sstinternal_tools.documents.mapper.interfaces.TagDtoMapper;
import org.springframework.stereotype.Component;

@Component
public class TagDtoMapperImpl implements TagDtoMapper{

    @Override
    public Tag fromCreateDto(TagCreateDto dto) {
        Tag tag = new Tag();
        tag.setName(dto.getName());
        return tag;
    }

    @Override
    public Tag fromUpdateDto(TagUpdateDto tagUpdateDto, Tag tag) {
        if(tagUpdateDto.getName() != null) {
            tag.setName(tagUpdateDto.getName());
        }
        return tag;
    }

    @Override
    public TagResponseDto toResponseDto(Tag tag) {
        return new TagResponseDto(tag.getId(), tag.getName());
    }

    @Override
    public TagSummaryDto toSummaryDto(Tag tag) {
        return new TagSummaryDto(tag.getName());
    }
}
