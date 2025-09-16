package com.sstinternaltools.sstinternal_tools.documents.service.implementation;

import com.sstinternaltools.sstinternal_tools.documents.service.interfaces.CloudStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.UUID;
import org.springframework.http.*;

@Service
public class CloudStorageServiceImpl implements CloudStorageService {

    @Value("${supabase.storage.bucket}")
    private String bucket;

    @Value("${supabase.project.url}")
    private String supabaseUrl;

    @Value("${supabase.api.key}")
    private String supabaseKey;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public String uploadFile(MultipartFile file) {
        try {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            String uploadUrl = supabaseUrl + "/storage/v1/object/" + bucket + "/" + fileName;
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + supabaseKey);

            String contentType = file.getContentType();
            if (file.getOriginalFilename().endsWith(".pdf")) {
                contentType = "application/pdf";
            }
            if (contentType == null || contentType.isBlank()) {
                contentType = "application/octet-stream";
            }
            headers.set("Content-Type", contentType);
            HttpEntity<byte[]> request = new HttpEntity<>(file.getBytes(), headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    uploadUrl,
                    HttpMethod.PUT,
                    request,
                    String.class
            );
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Failed to upload to Supabase: " + response.getBody());
            }
            // Return the URL to access the file
            return supabaseUrl + "/storage/v1/object/public/" + bucket + "/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload to Supabase", e);
        }
    }

    @Override
    public void deleteFile(String fileUrl) {
        try {
            String publicPrefix = "/storage/v1/object/public/";
            int index = fileUrl.indexOf(publicPrefix);
            if (index == -1) {
                throw new IllegalArgumentException("Invalid file URL: not a public Supabase file.");
            }
            String filePath = fileUrl.substring(index + publicPrefix.length());
            String deleteUrl = supabaseUrl + "/storage/v1/object/" + "/" + filePath;
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + supabaseKey);
            headers.set("apikey", supabaseKey);
            HttpEntity<Void> request = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    deleteUrl,
                    HttpMethod.DELETE,
                    request,
                    String.class
            );
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Failed to delete file from Supabase: " + response.getBody());
            }

        } catch (Exception e) {
            throw new RuntimeException("Error deleting file from Supabase", e);
        }
    }

}

