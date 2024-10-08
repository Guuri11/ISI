package di

import utils.AppContext
import utils.SpeechToText
import utils.SpeechToTextAndroid

actual fun createSpeechToText(): SpeechToText {
    return SpeechToTextAndroid(context = AppContext)
}