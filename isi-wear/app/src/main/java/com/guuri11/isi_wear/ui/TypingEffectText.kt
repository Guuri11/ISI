package com.guuri11.isi_wear.ui

import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text
import kotlinx.coroutines.delay

@Composable
fun TypingEffectText(
    fullText: String,
    typingSpeed: Long = 50L, // ms between each char
    cursorBlinkSpeed: Long = 80L, // ms between each cursor blink
    stopLottie: () -> Unit
) {
    var displayedText by remember { mutableStateOf("") }
    var cursorVisible by remember { mutableStateOf(true) }
    var isTextCompleted by remember { mutableStateOf(false) }

    // update text with typing effect
    LaunchedEffect(fullText) {
        displayedText = ""
        isTextCompleted = false
        for (char in fullText) {
            displayedText += char
            delay(typingSpeed)
        }
        isTextCompleted = true
        stopLottie()
        cursorVisible = false
    }

    // cursor handler
    LaunchedEffect(isTextCompleted) {
        if (!isTextCompleted) {
            while (true) {
                delay(cursorBlinkSpeed)
                cursorVisible = !cursorVisible
            }
        } else {
            cursorVisible = false
        }
    }
    Text(
        text = if (cursorVisible) "$displayedText|" else displayedText,
        fontSize = 16.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .absolutePadding(top = 80.dp)
    )
}