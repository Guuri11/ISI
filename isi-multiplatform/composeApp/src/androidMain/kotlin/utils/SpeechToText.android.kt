package utils

import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer

class SpeechToTextAndroid(private val context: AppContext) : SpeechToText {

    private val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context.get())
    private var onResult: ((String) -> Unit)? = null

    init {
        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) = Unit

            override fun onBeginningOfSpeech() = Unit

            override fun onRmsChanged(rmsdB: Float) = Unit

            override fun onBufferReceived(buffer: ByteArray?) = Unit

            override fun onEndOfSpeech() = Unit

            override fun onError(error: Int) = Unit

            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                onResult?.invoke(matches?.get(0) ?: "")
            }

            override fun onPartialResults(partialResults: Bundle?) = Unit

            override fun onEvent(eventType: Int, params: Bundle?) = Unit

        })
    }

    override fun startListening(onResult: (String) -> Unit) {
        this.onResult = onResult
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-ES")
        }
        speechRecognizer.startListening(intent)
    }

    override fun stopListening() {
        speechRecognizer.stopListening()
    }
}

actual fun createSpeechToText(): SpeechToText {
    return SpeechToTextAndroid(context = AppContext)
}
