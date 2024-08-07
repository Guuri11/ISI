package ui.componets.message

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import domain.entity.Command
import ui.theme.getColorsTheme

@Composable
fun UserMessage(command: Command, modifier: Modifier) {
    val colors = getColorsTheme()
    Text(
        text = command.content,
        color = colors.TextColor,
        fontSize = 16.sp,
        modifier = modifier,
    )
}