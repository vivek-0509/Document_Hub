package com.sstinternaltools.sstinternal_tools.documents.dto.tagDtos;

public class TagSummaryDto {
    String name;

    public TagSummaryDto(String name) {
        this.name = name;
    }

    public TagSummaryDto() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
