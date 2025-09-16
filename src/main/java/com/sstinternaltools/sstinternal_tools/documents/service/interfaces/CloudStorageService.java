package com.sstinternaltools.sstinternal_tools.documents.service.interfaces;

import org.springframework.web.multipart.MultipartFile;

public interface CloudStorageService {
    String uploadFile(MultipartFile file);
    void deleteFile(String fileUrl);
}
