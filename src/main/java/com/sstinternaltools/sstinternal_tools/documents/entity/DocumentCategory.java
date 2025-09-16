package com.sstinternaltools.sstinternal_tools.documents.entity;

import jakarta.persistence.*;

@Entity
public class DocumentCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(unique = true)
    private String normalizedName;

    public DocumentCategory(String name, String normalizedName) {
        this.name = name;
        this.normalizedName = normalizedName;
    }

    public DocumentCategory() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNormalizedName() {
        return normalizedName;
    }

    public void setNormalizedName(String normalizedName) {
        this.normalizedName = normalizedName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}