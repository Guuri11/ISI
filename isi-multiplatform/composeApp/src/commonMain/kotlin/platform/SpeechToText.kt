package platform

interface SpeechToText {
    fun startListening(onResult: (String) -> Unit)
    fun stopListening()
}

enum class IntentSpeechToText {
    NORMAL, ACTION
}

expect fun createSpeechToText(): SpeechToText