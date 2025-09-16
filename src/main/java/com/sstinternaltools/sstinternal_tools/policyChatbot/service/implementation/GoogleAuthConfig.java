//package com.sstinternaltools.sstinternal_tools.policyChatbot.service.implementation;
//
//import jakarta.annotation.PostConstruct;
//import org.springframework.stereotype.Component;
//
//import java.io.FileWriter;
//import java.io.IOException;
//
//@Component
//public class GoogleAuthConfig {
//
//    @PostConstruct
//    public void init() throws IOException {
//        String credentialsJson = System.getenv("GOOGLE_APPLICATION_CREDENTIALS_JSON");
//        if (credentialsJson != null && !credentialsJson.isEmpty()) {
//            String keyPath = "/tmp/gcp-key.json";
//            try (FileWriter writer = new FileWriter(keyPath)) {
//                writer.write(credentialsJson);
//            }
//            // Point the SDK to this temp file
//            System.setProperty("GOOGLE_APPLICATION_CREDENTIALS", keyPath);
//        }
//    }
//}
//
