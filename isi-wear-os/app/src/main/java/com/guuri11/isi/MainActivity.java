package com.guuri11.isi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.guuri11.isi.helpers.TaskManager.Task;
import com.guuri11.isi.helpers.AlarmHelper;
import com.guuri11.isi.helpers.Network.NetworkManager;
import com.guuri11.isi.helpers.VoiceManager;
import com.guuri11.isi.persistance.Emoji;
import com.guuri11.isi.persistance.ErrorMessage;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MainActivity extends Activity implements TextToSpeech.OnInitListener {

    private TextView isiMessage;
    private VoiceManager voiceManager;
    private NetworkManager networkManager;
    private AlarmHelper alarmHelper;
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    /**
     * The value could be any other number, is a code that we sent on startActivityForResult so whenever
     * we have a result, we check if is that code to know if the activity is related with STT
     */
    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isiMessage = findViewById(R.id.text);
        voiceManager = new VoiceManager(this, this);
        networkManager = new NetworkManager(this);
        alarmHelper = new AlarmHelper(this);

        voiceManager.startVoiceRecognition(REQUEST_CODE_SPEECH_INPUT);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            isiMessage.setText(Emoji.SALUTE_EMOJI);
        } else {
            isiMessage.setText("TTS Initialization Failed ⚠️");
        }
    }

    @Override
    protected void onDestroy() {
        voiceManager.stopTextToSpeech();
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdownNow();
            try {
                if (!executorService.awaitTermination(1, TimeUnit.MINUTES)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException ie) {
                executorService.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SPEECH_INPUT && resultCode == RESULT_OK && data != null) {
            List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            assert results != null;
            String result = results.get(0);
            Log.d("SpeechRecognition", "Result: " + result);
            manageCommand(result);
        }
    }

    private void manageCommand(String command) {
        if (Task.EXIT.options.contains(command.toLowerCase())) {
            finishAffinity();
            System.exit(0);
            return;
        }

        if (Task.ACTIVATE_LOCAL_ASSISTANT.options.contains(command.toLowerCase())) {
            NetworkManager.localAssistant = true;
            voiceManager.speak("Asistente local activado");
            return;
        }
        if (Task.ACTIVATE_REMOTE_ASSISTANT.options.contains(command.toLowerCase())) {
            NetworkManager.localAssistant = false;
            voiceManager.speak("Asistente remoto activado");
            return;
        }
        if (command.length() > 16 && Task.CREATE_ALARM.options.contains(command.substring(0, 17).toLowerCase())) {
            createAlarm(command);
            return;
        }
        sendCommand(command);
    }

    private void createAlarm(String command) {
        try {
            long triggerTime = alarmHelper.parseTime(command);
            alarmHelper.setAlarm(triggerTime);
            runOnUiThread(() -> {
                isiMessage.setText("Alarma configurada correctamente.");
                voiceManager.speak("Alarma configurada.");
            });
        } catch (IllegalArgumentException e) {
            runOnUiThread(() -> {
                isiMessage.setText(e.getMessage());
                voiceManager.speak(e.getMessage());
            });
        }
    }


    private void sendCommand(String command) {
        displayLoadingIndicator();

        Runnable sendTask = () -> networkManager.sendCommand(command, new NetworkManager.NetworkCallback() {
            @Override
            public void onCommandSuccess(String response) {
                runOnUiThread(() -> updateUIWithResponse(response));
            }

            @Override
            public void onCommandError(String error) {
                runOnUiThread(() -> showErrorMessage(error));
            }
        });

        executorService.submit(sendTask);
    }

    private void updateUIWithResponse(String response) {
        isiMessage.setText(Emoji.MIC_EMOJI);
        voiceManager.speak(response);
    }

    private void showErrorMessage(String error) {
        isiMessage.setText(error);
        voiceManager.speak(ErrorMessage.CONNECTION_ERROR + error);
    }

    private void displayLoadingIndicator() {
        isiMessage.setText(Emoji.THINKING_EMOJI);
    }
    public void startVoiceRecognition(View view) {
        voiceManager.startVoiceRecognition(REQUEST_CODE_SPEECH_INPUT);
    }
}