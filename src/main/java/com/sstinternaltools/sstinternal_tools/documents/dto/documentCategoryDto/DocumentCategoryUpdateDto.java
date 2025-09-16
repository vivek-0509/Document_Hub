package com.sstinternaltools.sstinternal_tools.documents.dto.documentCategoryDto;

public class DocumentCategoryUpdateDto {
    String name;

    public DocumentCategoryUpdateDto(String name) {
        this.name = name;
    }

    public DocumentCategoryUpdateDto() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
