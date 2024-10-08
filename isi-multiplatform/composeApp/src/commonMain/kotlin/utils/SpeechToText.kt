package utils

interface SpeechToText {
    fun startListening(onResult: (String) -> Unit)
    fun stopListening()
}

expect fun createSpeechToText(): SpeechToText