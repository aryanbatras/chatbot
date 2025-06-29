package com.jaruratcare.service;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FirebaseService {

    public void save(String from, String text){
        try {
            System.out.println("📝 Attempting to save message to Firebase");
            System.out.println("📞 From: " + from);
            System.out.println("💬 Text: " + text);

            System.out.println("Getting firestore client" );
            System.out.println("Apps: " + FirebaseApp.getApps());
            System.out.println("Is default app initialized? " + FirebaseApp.getInstance().getName());
            Firestore db = FirestoreClient.getFirestore();

            if (db == null) {
                System.out.println("❌ Firestore DB is null! Firebase may not be initialized.");
                return;
            }

            if (FirebaseApp.getApps().isEmpty()) {
                System.out.println("🚫 FirebaseApp not initialized. Skipping save.");
                return;
            }

            Map<String, Object> data = new HashMap<>();
            data.put("number", from);
            data.put("message", text);
            data.put("timestamp", System.currentTimeMillis());

            DocumentReference ref = db.collection("messages").document();
            System.out.println("📂 Firebase doc reference created: " + ref.getId());

            ref.set(data).get();
            System.out.println("✅ Message saved to Firebase (confirmed)");
        } catch (Exception e) {
            System.out.println("❌ Firebase write failed");
            e.printStackTrace();
        }
    }



}
