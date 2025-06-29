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

            String decodedJson = new String(Base64.getDecoder().decode(base64Creds), StandardCharsets.UTF_8);

            decodedJson = decodedJson.replace("\\n", "\n");
            System.out.println("Decoded bytes after replacing" + decodedJson );

            InputStream stream = new ByteArrayInputStream(decodedJson.getBytes(StandardCharsets.UTF_8));

            GoogleCredentials credentials = GoogleCredentials
                    .fromStream(stream);
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
