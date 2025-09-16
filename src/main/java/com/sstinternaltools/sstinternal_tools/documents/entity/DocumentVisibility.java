package com.sstinternaltools.sstinternal_tools.documents.entity;

import jakarta.persistence.*;

@Entity
public class DocumentVisibility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Document document;

    private AllowedUsers allowedUsers; // STUDENT, TEACHER, ADMIN
}
