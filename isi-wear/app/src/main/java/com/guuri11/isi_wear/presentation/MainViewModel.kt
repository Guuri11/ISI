package com.guuri11.isi_wear.presentation

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guuri11.isi_wear.utils.NetworkManager
import com.guuri11.isi_wear.domain.ErrorMessage
import com.guuri11.isi_wear.domain.Emoji
import com.guuri11.isi_wear.domain.Task
import com.guuri11.isi_wear.utils.alarm.AlarmService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale
import kotlin.system.exitProcess

class MainViewModel(
    private val alarmService: AlarmService,
    private val networkManager: NetworkManager,
    var textToSpeech: TextToSpeech,
    private val applicationContext: Context
) : ViewModel() {

    var isiText by mutableStateOf("")
    var isiIsTalking by mutableStateOf(false)

    init {
        setupTextToSpeech()
    }

    private fun setupTextToSpeech() {
        textToSpeech = TextToSpeech(applicationContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = textToSpeech.setLanguage(Locale("es", "ES"))
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e(
                        MainViewModel::class.simpleName,
                        ErrorMessage.LANGUAGE_NOT_SUPPORTED
                    )
                    isiText = ErrorMessage.LANGUAGE_NOT_SUPPORTED
                } else {
                    Log.i(MainViewModel::class.simpleName, "TTS Language is set to Spanish")
                }
            } else {
                Log.e(MainViewModel::class.simpleName, "TTS Initialization failed with status -> $status")
                isiText = "TTS Initialization failed"
            }
        }
    }

    fun handleCommand(command: String) {
        val lowerCaseCommand = command.lowercase(Locale.getDefault())
        when {
            Task.EXIT.options.contains(lowerCaseCommand) -> exitApp()
            Task.ACTIVATE_LOCAL_ASSISTANT.options.contains(lowerCaseCommand) -> activateLocalAssistant()
            Task.ACTIVATE_REMOTE_ASSISTANT.options.contains(lowerCaseCommand) -> activateRemoteAssistant()
            command.length > 16 && Task.CREATE_ALARM.options.contains(
                lowerCaseCommand.substring(
                    0,
                    17
                )
            ) -> createAlarm(command)

            else -> sendCommand(command)
        }
    }

    private fun speak(mic: Boolean, content: String) {
        viewModelScope.launch(Dispatchers.Default) {
            isiText = if (mic) Emoji.MIC_EMOJI else content
            isiIsTalking = true
            textToSpeech.speak(content, TextToSpeech.QUEUE_FLUSH, null, null)
            Log.i("ISI Speaking", content)
        }
    }


    private fun exitApp() {
        exitProcess(0)
    }

    private fun activateLocalAssistant() {
        NetworkManager.localAssistant = true
        speak(true, "Asistente local activado")
    }

    private fun activateRemoteAssistant() {
        NetworkManager.localAssistant = false
        speak(true, "Asistente remoto activado")
    }

    private fun createAlarm(command: String) {
        try {
            val triggerTime: Long = alarmService.parseTime(command)
            alarmService.setAlarm(triggerTime)
            speak(false, "Alarma configurada")
        } catch (e: IllegalArgumentException) {
            speak(false, e.message.toString())
        }
    }

    private fun sendCommand(command: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response =
                    networkManager.sendCommand(command, object : NetworkManager.NetworkCallback {
                        override fun onCommandSuccess(response: String) {
                            viewModelScope.launch(Dispatchers.Main) { speak(false, response) }
                        }

                        override fun onCommandError(error: String) {
                            viewModelScope.launch(Dispatchers.Main) {
                                handleCommandError(error, command)
                            }
                        }
                    })
                println(response)
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    speak(false, e.message ?: ErrorMessage.UNKNOWN_ERROR)
                }
            }
        }
    }

    private fun handleCommandError(error: String, command: String) {
        if (error.contains(ErrorMessage.CONNECTED_REFUSED) && !NetworkManager.localAssistant) {
            speak(false, ErrorMessage.CONNECTION_ERROR_TRYING_WITH_LOCAL_ASSISTANT)
            NetworkManager.localAssistant = true
            sendCommand(command)
        } else {
            speak(false, ErrorMessage.CONNECTION_ERROR + error)
        }
    }
}
