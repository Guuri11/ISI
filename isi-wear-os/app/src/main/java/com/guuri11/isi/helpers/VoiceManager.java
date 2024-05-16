package com.guuri11.isi.helpers;

import android.app.Activity;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

public class VoiceManager {

    private final TextToSpeech textToSpeech;
    private final Activity activity;

    public VoiceManager(Activity activity, TextToSpeech.OnInitListener listener) {
        this.activity = activity;
        textToSpeech = new TextToSpeech(activity, listener);
        textToSpeech.setLanguage(new Locale("es", "ES"));
    }

    public void speak(String text) {
        Log.d("speak out", "text: " + text);
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    public void stopTextToSpeech() {
        textToSpeech.stop();
        textToSpeech.shutdown();
    }

    public void startVoiceRecognition(int requestCode) {
        textToSpeech.stop();
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        activity.startActivityForResult(intent, requestCode);
    }
}
