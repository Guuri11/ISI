package ui.componets

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import ui.theme.getColorsTheme

@Composable
fun LoadingText() {
    val colors = getColorsTheme()
    Text(
        "Loading data...",
        color = colors.TextColor,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
    )
}