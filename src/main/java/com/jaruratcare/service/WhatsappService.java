package com.jaruratcare.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.jaruratcare.secret.Token;

import javax.net.ssl.HttpsURLConnection;
import java.net.MalformedURLException;
import java.io.OutputStream;
import java.io.IOException;
import java.util.List;
import java.net.URL;
import java.util.Map;

@Service
public class WhatsappService {

    private String url;
    public String token;

    WhatsappService(){
        this.url = Token.url;
        this.token = Token.token;
    }

    public int sendTextMessage(String number, String message){

        try{
            URL ob = new URL(url);
            HttpsURLConnection connection;
            connection = (HttpsURLConnection) ob.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            String json = "{\n" +
                    "  \"messaging_product\": \"whatsapp\",\n" +
                    "  \"to\": \"" + number + "\",\n" +
                    "  \"type\": \"text\",\n" +
                    "  \"text\": {\n" +
                    "    \"body\": \"" + message + "\"\n" +
                    "  }\n" +
                    "}";

            OutputStream os = connection.getOutputStream();
            os.write(json.getBytes());
            os.flush();
            os.close();

           return connection.getResponseCode();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public  String getSmartReplyFromCloudflare(String userMessage) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer f0eb2Zk3fpdeFhEQesTeH-xR4soOWrKhoye57AT9");

            Map<String, Object> systemMsg = Map.of(
                    "role", "system",
                    "content", "You are a friendly assistant from the Jarurat Care Foundation who understands that facing cancer can be overwhelming, and no one should have to go through it alone. That’s why you're here to stand by those affected by cancer, offering unwavering support every step of the way. You focus on creating a warm and inclusive space where everyone’s needs are heard and met "
            );

            Map<String, Object> userMsg = Map.of(
                    "role", "user",
                    "content", userMessage
            );

            Map<String, Object> requestBody = Map.of(
                    "messages", List.of(systemMsg, userMsg)
            );

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

            String url = "https://api.cloudflare.com/client/v4/accounts/7dad86f8a0527be2fbe5c947d3d33550/ai/run/@cf/mistral/mistral-7b-instruct-v0.2-lora";

            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map result = (Map) response.getBody().get("result");
                String aiReply = (String) result.get("response");
                return aiReply;
            }

        } catch (Exception e) {
            System.out.println("⚠️ AI fallback triggered: " + e.getMessage());
        }

        return "Okay, we’ll get back to you shortly 😊";
    }


}
