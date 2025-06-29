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

import jakarta.annotation.PostConstruct;

@Component
public class FirebaseInitializer {

    @PostConstruct
    public void initialize() {
        try {

            String credentialsJson = System.getenv("GOOGLE_CREDENTIALS_JSON");
            System.out.println("GOT IT: " + credentialsJson );

            GoogleCredentials credentials = GoogleCredentials.fromStream(
                    new ByteArrayInputStream(credentialsJson.getBytes(StandardCharsets.UTF_8))
            );
            System.out.println("GoogleCredentials in Bytes" );

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(credentials)
                    .build();
            System.out.println("Firebase building up" );

            FirebaseApp.initializeApp(options);
            System.out.println("Firebase initialized" );

        } catch (Exception e) {
            System.out.println("❌ Failed to initialize Firebase");
            e.printStackTrace();
        }
    }
}
