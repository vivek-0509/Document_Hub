package com.sstinternaltools.sstinternal_tools.documents.dto.tagDtos;

public class TagResponseDto {
    Long id;
    String name;

    public TagResponseDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public TagResponseDto() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
