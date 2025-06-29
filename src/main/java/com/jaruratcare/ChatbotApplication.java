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
			if (!file.exists()) {
				throw new FileNotFoundException(" Firebase credentials file not found at " + filePath);
			}
			InputStream serviceAccount = new FileInputStream(file);
			FirebaseOptions options = FirebaseOptions.builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccount))
					.build();
			if (FirebaseApp.getApps().isEmpty()) {
				FirebaseApp.initializeApp(options);
				System.out.println("✅ Firebase initialized!");
			}
		} catch (Exception e) {
			System.out.println("❌ Failed to initialize Firebase");
			e.printStackTrace();
		}
	}
}
