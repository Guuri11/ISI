package utils

import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.TargetDataLine
import java.io.ByteArrayOutputStream

class SpeechToTextDesktop : SpeechToText {

    private var targetDataLine: TargetDataLine? = null

    override fun startListening(onResult: (String) -> Unit) {
        val format = AudioFormat(16000.0f, 16, 1, true, true)
        targetDataLine = AudioSystem.getTargetDataLine(format).also {
            it.open(format)
            it.start()

            val buffer = ByteArray(1024)
            val outputStream = ByteArrayOutputStream()

            // Replace with actual speech recognition logic
            Thread {
                while (true) {
                    val bytesRead = it.read(buffer, 0, buffer.size)
                    outputStream.write(buffer, 0, bytesRead)

                    // Here you would send the audio data to a speech recognition service
                    // For demonstration purposes, we’ll just call onResult with dummy text
                    onResult("Recognized Speech")
                }
            }.start()
        }
    }

    override fun stopListening() {
        targetDataLine?.stop()
        targetDataLine?.close()
        targetDataLine = null
    }
}
