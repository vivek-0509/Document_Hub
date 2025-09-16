package com.sstinternaltools.sstinternal_tools.documents.dto.documentVersionDtos;

import org.springframework.web.multipart.MultipartFile;

public class DocumentVersionUpdateDto {

    private MultipartFile file;
    private Boolean isLatestVersion;

    public Boolean getLatestVersion() {
        return isLatestVersion;
    }

    public void setLatestVersion(Boolean latestVersion) {
        isLatestVersion = latestVersion;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
