package com.jaruratcare.service;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FirebaseService {

    public void save(String from, String text){
        try{
            Firestore db = FirestoreClient.getFirestore();
            Map<String, Object> data = new HashMap<>();

            data.put("number", from);
            data.put("message", text);
            data.put("timestamp", System.currentTimeMillis());

            DocumentReference ref = db.collection("messages").document();
            ref.set(data);

            System.out.println("✅ Message saved to Firebase");

        } catch (Exception e) {
            System.out.println("❌ Firebase write failed");
            e.printStackTrace();
        }

    }


}
