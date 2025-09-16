package com.sstinternaltools.sstinternal_tools.documents.dto.documentCategoryDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class DocumentCategoryResponseDto {
    @NotNull(message="Category Id cannot be null")
    private Long id;
    @NotBlank(message = "Category name cannot be null")
    private String name;
    @NotBlank(message = "Category normalized name cannot be null")
    private String normalizedName;


    public DocumentCategoryResponseDto(Long id, String name,String normalizedName) {
        this.id = id;
        this.name = name;
        this.normalizedName = normalizedName;
    }

    public DocumentCategoryResponseDto() {}

    public String getNormalizedName() {
        return normalizedName;
    }

    public void setNormalizedName(String normalizedName) {
        this.normalizedName = normalizedName;
    }

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
