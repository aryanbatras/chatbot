package com.jaruratcare.service;

import org.springframework.stereotype.Service;
import com.jaruratcare.secret.Token;

import java.net.URL;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import javax.net.ssl.HttpsURLConnection;

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

}
