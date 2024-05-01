package com.guuri11.isi.helpers.Network;

import android.content.Context;

import com.guuri11.isi.dto.CommandDto;
import com.google.gson.Gson;
import com.guuri11.isi.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.UUID;

public class NetworkManager {
    private final Context context;
    private UUID chatId;
    private final Gson gson;
    public static boolean localAssistant = false;

    public interface NetworkCallback {
        void onCommandSuccess(String response);
        void onCommandError(String error);
    }

    public NetworkManager(Context context) {
        this.context = context;
        this.gson = new Gson();
    }

    public void sendCommand(String command, NetworkCallback callback) {
        boolean isConnectedToExpectedWifi = WiFiUtils.isConnectedToWifi(context, "\"" + context.getString(R.string.wifi_ssid) + "\"");
        if (isConnectedToExpectedWifi && !localAssistant) {
            sendToBackend(command, callback);
        } else {
            sendToLocalGPT(command, callback);
        }
    }

    private void sendToBackend(String command, NetworkCallback callback) {
        HTTPService.sendCommand(command, new HTTPService.Callback() {
            @Override
            public void onSuccess(InputStream inputStream) {
                String responseStr = convertInputStreamToString(inputStream);
                CommandDto commandDto = gson.fromJson(responseStr, CommandDto.class);
                chatId = UUID.fromString(commandDto.getChat().getId());
                callback.onCommandSuccess(commandDto.getContent());
            }

            @Override
            public void onError(String errorMessage) {
                callback.onCommandError(errorMessage);
            }
        }, chatId);
    }

    private void sendToLocalGPT(String command, NetworkCallback callback) {
        HTTPService.gptLocal(command, new HTTPService.GptLocalCallback() {
            @Override
            public void onResponse(String response) {
                callback.onCommandSuccess(response);
            }

            @Override
            public void onError(String errorMessage) {
                callback.onCommandError(errorMessage);
            }
        }, context);
    }

    private String convertInputStreamToString(InputStream inputStream) {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
        } catch (IOException e) {
            return ""; // Or handle the exception as needed for your application
        }
        return stringBuilder.toString();
    }
}