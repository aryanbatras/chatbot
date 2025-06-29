package com.jaruratcare;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.jaruratcare.service.WhatsappService;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.*;

@SpringBootApplication
public class ChatbotApplication {

	public static void main(String[] args) {
		firebaseInitializer();
		SpringApplication.run(ChatbotApplication.class, args);
	}

	private static void firebaseInitializer() {
		try {
			String filePath = "/app/jaruratcare-db-firebase.json";
			File file = new File(filePath);
			System.out.println("🧾 Checking for Firebase credential file at: " + filePath);

			if (!file.exists()) {
				System.out.println("❌ Firebase credentials file NOT FOUND!");
				throw new FileNotFoundException(" Firebase credentials file not found at " + filePath);
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
