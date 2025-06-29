package com.jaruratcare.config;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.stereotype.Component;
import com.google.auth.oauth2.GoogleCredentials;

import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import jakarta.annotation.PostConstruct;

@Component
public class FirebaseInitializer {

    @PostConstruct
    public void initialize() {
        try {
            String filePath = "/app/jaruratcare-db-firebase.json";
            File file = new File(filePath);
            System.out.println("🧾 Checking for Firebase credential file at: " + filePath);

            if (!file.exists()) {
                System.out.println("❌ Firebase credentials file NOT FOUND!");
                throw new RuntimeException("Firebase credentials not found at " + filePath);
            } else {
                System.out.println("✅ Firebase credentials file FOUND!");
            }

            InputStream serviceAccount = new FileInputStream(file);
            System.out.println("📦 Reading Firebase service account stream...");

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("✅ Firebase initialized!");
            } else {
                System.out.println("ℹ️ Firebase already initialized, skipping...");
            }

        } catch (Exception e) {
            System.out.println("❌ Failed to initialize Firebase");
            e.printStackTrace();
        }
    }
}
