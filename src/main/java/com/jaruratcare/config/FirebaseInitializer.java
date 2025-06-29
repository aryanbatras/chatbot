package com.jaruratcare.config;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.stereotype.Component;
import com.google.auth.oauth2.GoogleCredentials;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import jakarta.annotation.PostConstruct;

@Component
public class FirebaseInitializer {

    @PostConstruct
    public void initialize() {
        try {

            String base64Creds = System.getenv("FIREBASE_CREDENTIALS_BASE64");
            System.out.println("Base64: " + base64Creds );

            byte[] decodedBytes = Base64.getDecoder().decode(base64Creds);
            System.out.println("Decoded bytes " + decodedBytes );

            GoogleCredentials credentials = GoogleCredentials
                    .fromStream(new ByteArrayInputStream(decodedBytes));
            System.out.println("GoogleCredentials done" );

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(credentials)
                    .setProjectId("jaruratcare-db")
                    .build();
            System.out.println("Firebase options done" );

            FirebaseApp.initializeApp(options);
            System.out.println("Firebase initialized" );

        } catch (Exception e) {
            System.out.println("❌ Failed to initialize Firebase");
            e.printStackTrace();
        }
    }
}
