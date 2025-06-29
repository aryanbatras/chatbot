package com.jaruratcare;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.io.InputStream;

@SpringBootApplication
public class ChatbotApplication {

	public static void main(String[] args) {
		firebaseInitializer();
		SpringApplication.run(ChatbotApplication.class, args);
	}

	private static void firebaseInitializer() {
		try (InputStream serviceAccount = ChatbotApplication.class.getResourceAsStream("/jaruratcare-db-firebase.json")) {
			FirebaseOptions options = FirebaseOptions.builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccount))
					.build();

			if (FirebaseApp.getApps().isEmpty()) {
				FirebaseApp.initializeApp(options);
				System.out.println("✅ Firebase initialized!");
			}

		} catch (IOException e) {
			System.out.println("❌ Failed to initialize Firebase");
			e.printStackTrace();
		}
	}

}
