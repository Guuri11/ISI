package com.guuri11.isi.helpers.Network;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.guuri11.isi.R;
import com.guuri11.isi.persistance.ErrorMessage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import dev.ai4j.openai4j.OpenAiClient;
import dev.ai4j.openai4j.chat.ChatCompletionModel;
import dev.ai4j.openai4j.chat.ChatCompletionRequest;
import dev.ai4j.openai4j.chat.Message;

public class HTTPService {

    private static final String BASE_URL = "http://192.168.1.76:8080/api/v1/commands";

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
                    callback.onError(ErrorMessage.CANT_PERFOM_REQUEST + responseCode);
                }
            } catch (IOException e) {
                callback.onError(e.getMessage());
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
            jsonInputString = "{\"request\": \"" + result + "\", \"chat\": {\"id\": \"" + chatId + "\"}}";
        } else {
            jsonInputString = "{\"request\": \"" + result + "\"}";
        }

        conn.setDoOutput(true);
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        return conn;
    }

    public interface GptLocalCallback {
        void onResponse(String response);

        void onError(String errorMessage);
    }

    public static void gptLocal(List<Message> messages, GptLocalCallback callback, Context context) {
        String openAiKey = context.getString(R.string.openai_key);
        Log.d("HTTPService", "OpenAI API Key: " + openAiKey);

        OpenAiClient client = OpenAiClient.builder()
                .openAiApiKey(openAiKey)
                .build();

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model(ChatCompletionModel.GPT_3_5_TURBO_0125)
                .messages(messages)
                .build();

        client.chatCompletion(request)
                .onResponse(response -> callback.onResponse(response.content()))
                .onError(error -> callback.onError(error.getMessage()))
                .execute();
    }
}

