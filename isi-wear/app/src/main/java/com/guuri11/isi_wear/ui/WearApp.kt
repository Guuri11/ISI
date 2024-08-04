package com.guuri11.isi_wear.ui

import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.MaterialTheme
import com.guuri11.isi_wear.presentation.MainViewModel
import com.guuri11.isi_wear.theme.IsiwearTheme

@Composable
fun WearApp(viewModel: MainViewModel) {
    val voiceLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { activityResult ->
            activityResult.data?.let { data ->
                val results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                results?.get(0)?.let {
                    viewModel.handleCommand(it)
                }
            }
        }
    val voiceIntent: Intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
    }

    IsiwearTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            contentAlignment = Alignment.Center
        ) {
            ISIRobot(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                        viewModel.textToSpeech.stop()
                        viewModel.isiIsTalking = false
                        voiceLauncher.launch(voiceIntent)
                    },
                isPlaying = viewModel.isiIsTalking,
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()) // scroll for long texts
                    .padding(
                        horizontal = 16.dp,
                        vertical = 60.dp
                    ) // padding to avoid overlap from screen borders
                    .align(Alignment.Center)
                    .clickable {
                        viewModel.textToSpeech.stop()
                        viewModel.isiIsTalking = false
                        voiceLauncher.launch(voiceIntent)
                    }
            ) {
                TypingEffectText(fullText = viewModel.isiText, stopLottie = {
                    viewModel.isiIsTalking = false
                })

            }
        }
    }
}