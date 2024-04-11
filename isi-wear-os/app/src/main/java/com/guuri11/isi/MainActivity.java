package com.guuri11.isi;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.guuri11.isi.Dto.CommandDto;
import com.guuri11.isi.Network.HTTPService;
import com.guuri11.isi.TaskManager.GreetingHandler;
import com.guuri11.isi.TaskManager.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class MainActivity extends Activity implements TextToSpeech.OnInitListener {

    private TextView isiMessage;
    private TextToSpeech textToSpeech;
    private static UUID chatId = null;
    private final GreetingHandler greetingHandler = new GreetingHandler();
    /**
     * The value could be any other number, is a code that we sent on startActivityForResult so whenever
     * we have a result, we check if is that code to know if the activity is related with STT
     */
    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textToSpeech = new TextToSpeech(this, this);
        isiMessage = findViewById(R.id.text);
    }

    @SuppressLint("SetTextI18n")
    @Override // TTS
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            textToSpeech.setLanguage(new Locale("es", "ES"));
            speakOut(greetingHandler.getGreeting());
        } else {
            Log.e("TTS onInit", "Error: could not init - Line 92");
            isiMessage.setText("TTS error  ⚠️\uD83D\uDE3F");
        }
    }

    @Override // TTS
    public void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SPEECH_INPUT && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String result = results.get(0);
            Log.d("SpeechRecognition", "Result: " + result);
            manageCommand(result);
        }
    }

    private void manageCommand(final String command) {
        if (Task.HELLO.options.contains(command.toLowerCase())) {
            Log.d("manageCommand", "command: " + Task.HELLO.name());
            speakOut(greetingHandler.getGreeting());
            return;
        }
        if (Task.EXIT.options.contains(command.toLowerCase())) {
            Log.d("manageCommand", "command: " + Task.EXIT.name());
            finishAffinity();
            System.exit(0);
            return;
        }

        // Task ask to AI
        sendCommand(command);
    }

    private void sendCommand(String result) {
        isiMessage.setText("Pensando... \uD83D\uDCAD");
        HTTPService.sendCommand(result, new HTTPService.Callback() {
            @Override
            public void onSuccess(InputStream inputStream) {
                runOnUiThread(() -> {
                    Log.d("HTTPService", "Success, received stream");
                    String response = convertInputStreamToString(inputStream);
                    Log.d("HTTPService response", response);

                    Gson gson = new Gson();
                    CommandDto commandDto = gson.fromJson(response, CommandDto.class);
                    chatId = UUID.fromString(commandDto.getChat().getId());
                    // Icon for microphone
                    isiMessage.setText("\uD83C\uDF99\uFE0F");
                    speakOut(commandDto.getContent());
                });
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() -> {
                    Log.e("HTTPService", "Error: " + errorMessage);
                    speakOut("Error en la conexión: " + errorMessage);
                    isiMessage.setText(errorMessage);
                });
            }
        }, chatId);
    }

    private String convertInputStreamToString(InputStream inputStream) {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
        } catch (IOException e) {
            Log.e("HTTPService", "Error converting InputStream to String", e);
        }
        return stringBuilder.toString();
    }


    private void speakOut(String text) {
        Log.d("speak out", "text: " + text);
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    /**
     * Called when we click on the Lottie
     */
    public void startVoiceRecognition(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
    }

}

