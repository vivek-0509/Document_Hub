package com.sstinternaltools.sstinternal_tools.documents.dto.tagDtos;

public class TagUpdateDto {
    String name;

    public TagUpdateDto(String name) {
        this.name = name;
    }

    public TagUpdateDto() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
