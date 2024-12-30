package org.guuri11.isimultiplatform

import App
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import platform.AppContext
import platform.IntentSpeechToText

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = handleIntent(intent)

        AppContext.setUp(applicationContext)
        setContent {
            App(intent)
        }
    }

    private fun handleIntent(intent: Intent?): IntentSpeechToText {
        intent?.let {
            when (it.action) {
                Intent.ACTION_ASSIST -> {
                    return IntentSpeechToText.ACTION
                }
                Intent.ACTION_VOICE_COMMAND -> {
                    return IntentSpeechToText.ACTION
                }
                else -> {
                    return IntentSpeechToText.NORMAL
                }
            }
        }
        return IntentSpeechToText.NORMAL
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}