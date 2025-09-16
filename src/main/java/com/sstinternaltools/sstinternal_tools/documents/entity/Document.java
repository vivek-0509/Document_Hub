package com.sstinternaltools.sstinternal_tools.documents.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_id",nullable = false)
    private Long id;

    @Column(nullable = false)
    private String title;

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id")
    private DocumentCategory category;

    @Enumerated(EnumType.STRING)
    @ElementCollection
    private Set<AllowedUsers> allowedUsers;

    @ManyToMany
    @JoinTable(
            name = "document_tags", // Name of the join table
            joinColumns = @JoinColumn(name = "document_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags;
    private LocalDateTime createdAt;

    public Document(String title, DocumentCategory category, Set<AllowedUsers> allowedUsers, Set<Tag> tags,LocalDateTime createdAt) {
        this.title = title;
        this.category = category;
        this.allowedUsers = allowedUsers;
        this.tags = tags;
        this.createdAt = createdAt;
    }

    public Document() {}

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public DocumentCategory getCategory() {
        return category;
    }

    public void setCategory(DocumentCategory category) {
        this.category = category;
    }

    public Set<AllowedUsers> getAllowedUsers() {
        return allowedUsers;
    }

    public void setAllowedUsers(Set<AllowedUsers> allowedUsers) {
        this.allowedUsers = allowedUsers;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }
}
