package com.guuri11.isi_wear.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.guuri11.isi_wear.utils.alarm.AlarmService
import android.speech.tts.TextToSpeech
import com.guuri11.isi_wear.utils.NetworkManager

class MainViewModelFactory(
    private val alarmService: AlarmService,
    private val networkManager: NetworkManager,
    private val textToSpeech: TextToSpeech,
    private val applicationContext: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(alarmService, networkManager, textToSpeech, applicationContext) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
