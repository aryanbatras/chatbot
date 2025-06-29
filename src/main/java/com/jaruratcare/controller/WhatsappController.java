package com.jaruratcare.controller;

import com.jaruratcare.service.FirebaseService;
import org.springframework.http.ResponseEntity;
import com.jaruratcare.service.WhatsappService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class WhatsappController {

    @Autowired
    private WhatsappService service;

    @Autowired
    private FirebaseService firebaseService;

    @GetMapping("/health")
    public String health() {
        return "Hello from JaruratCare Bot!";
    }

    @GetMapping("/send")
    public String send(@RequestParam String number, @RequestParam String message){
        int res = service.sendTextMessage(number, message);
        return "Sent message to " + number + " with response " + res;
    }

    @GetMapping("/webhook")
    public ResponseEntity<String> verify(@RequestParam(name = "hub.mode") String mode,
                                         @RequestParam(name = "hub.verify_token") String token,
                                         @RequestParam(name = "hub.challenge") String challenge){
        String VERIFY_TOKEN = "jaruratcare";
        if ("subscribe".equals(mode) && VERIFY_TOKEN.equals(token)) {
            System.out.println("✅ Webhook verified by Meta!");
            return ResponseEntity.ok(challenge);
        } else {
            System.out.println("❌ Webhook verification failed!");
            return ResponseEntity.status(403).body("Verification failed");
        }
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> receiveMessage(@RequestBody Map<String, Object> body) {
        try {
            Map entry = ((List<Map>) body.get("entry")).get(0);
            Map changes = ((List<Map>) entry.get("changes")).get(0);
            Map value = (Map) changes.get("value");

            Map message = ((List<Map>) value.get("messages")).get(0);
            Map contact = ((List<Map>) value.get("contacts")).get(0);

            String from = (String) message.get("from");
            String text = (String) ((Map) message.get("text")).get("body");

            System.out.println("📥 Message received!");
            System.out.println("📞 From: " + from);
            System.out.println("💬 Text: " + text);

            String aiReply = service.getSmartReplyFromCloudflare(text);

            int statusCode = service.sendTextMessage(from, aiReply);
            System.out.println("📤 WhatsApp send status: " + statusCode);

            firebaseService.save(from, text);

            return ResponseEntity.ok("Message processed");

        } catch (Exception e) {
            System.out.println("❌ Error in webhook processing");
            e.printStackTrace();
            return ResponseEntity.ok("Ignored");
        }
    }


}
