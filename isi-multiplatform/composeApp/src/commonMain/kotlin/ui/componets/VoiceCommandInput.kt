package ui.componets

import androidx.compose.runtime.*
import platform.createSpeechToText

@Composable
fun VoiceCommandInput(sendMessage: (String) -> Unit) {
    val speechToText = createSpeechToText()

    LaunchedEffect(Unit) {
        speechToText.startListening { result ->
            sendMessage(result)
        }
    }
}