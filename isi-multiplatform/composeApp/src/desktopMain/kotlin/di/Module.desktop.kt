package di

import utils.SpeechToText
import utils.SpeechToTextDesktop

actual fun createSpeechToText(): SpeechToText {
    return SpeechToTextDesktop()
}