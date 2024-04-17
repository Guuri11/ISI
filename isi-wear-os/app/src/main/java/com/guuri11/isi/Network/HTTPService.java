package com.guuri11.isi.Network;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class HTTPService {

    private static final String BASE_URL = "http://192.168.1.198:8080/api/v1/commands";

    public interface Callback {
        void onSuccess(InputStream inputStream);
        void onError(String error);
    }

    public static void sendCommand(String result, Callback callback, UUID chatId) {
        new Thread(() -> {
            try {
                URL url = new URL(BASE_URL);
                HttpURLConnection conn = getHttpURLConnection(result, chatId, url);

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    callback.onSuccess(conn.getInputStream());
                } else {
                    callback.onError("Error al hacer la solicitud: " + responseCode);
                }
            } catch (IOException e) {
                callback.onError("Exception: " + e.getMessage());
            }
        }).start();
    }

    @NonNull
    private static HttpURLConnection getHttpURLConnection(String result, UUID chatId, URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Content-Type", "application/json");

        String jsonInputString;

        if (chatId != null) {
            jsonInputString = "{\"request\": \"" + result + "\", \"chat\": {\"id\": \"" + chatId.toString() + "\"}}";
        } else {
            jsonInputString = "{\"request\": \"" + result + "\"}";
        }

        conn.setDoOutput(true);
        try(OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        return conn;
    }
}

