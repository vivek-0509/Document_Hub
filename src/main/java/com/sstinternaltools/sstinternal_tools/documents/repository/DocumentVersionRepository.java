package com.sstinternaltools.sstinternal_tools.documents.repository;

import com.sstinternaltools.sstinternal_tools.documents.entity.DocumentVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DocumentVersionRepository extends JpaRepository<DocumentVersion, Long> {

    DocumentVersion getByDocumentId(Long documentId);
    DocumentVersion findByDocumentIdAndIsLatestVersionTrue(Long documentId);
    DocumentVersion findTopByDocumentIdOrderByVersionNumberDesc(Long documentId);
    void deleteAllByDocumentId(Long documentId);
    List<DocumentVersion> findByDocumentIdOrderByVersionNumberDesc(Long documentId);
}
