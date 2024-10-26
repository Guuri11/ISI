package ui.componets

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun LoadingText() {
    Text(
        "Loading data...",
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
    )
}