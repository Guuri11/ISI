package ui.componets.message

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import domain.entity.Command
import ui.theme.getColorsTheme

@Composable
fun AssistantMessage(command: Command) {
    val colors = getColorsTheme()
    Text(
        text = command.content,
        color = colors.TextColor,
        fontSize = 16.sp,
        modifier = Modifier.padding(vertical = 8.dp)
            .padding(all = 8.dp)
    )
}