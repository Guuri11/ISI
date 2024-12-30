package com.guuri11.isi_wear

import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.guuri11.isi_wear.presentation.MainViewModel
import com.guuri11.isi_wear.presentation.MainViewModelFactory
import com.guuri11.isi_wear.ui.WearApp
import com.guuri11.isi_wear.data.service.NetworkManager
import com.guuri11.isi_wear.data.service.WearAlarmService
import kotlin.system.exitProcess

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val factory = MainViewModelFactory(
                WearAlarmService(this),
                NetworkManager(this),
                TextToSpeech(applicationContext, null),
                applicationContext
            )
            viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

            setContent {
                WearApp(viewModel = viewModel)
            }
        } else {
            // Fallback or error handling if necessary
            exitProcess(0)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::viewModel.isInitialized) {
            viewModel.textToSpeech.stop()
            viewModel.textToSpeech.shutdown()
        }
    }
}
