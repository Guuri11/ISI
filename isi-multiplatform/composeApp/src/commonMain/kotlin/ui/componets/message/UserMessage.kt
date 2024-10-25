package ui.componets.message

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import domain.entity.Command

@Composable
fun UserMessage(command: Command, modifier: Modifier) {
    Text(
        text = command.content,
        fontSize = 16.sp,
        modifier = modifier,
    )
}