package ui.componets.message

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import contentScale.ContentScale
import domain.entity.Command
import ui.theme.getColorsTheme
import utils.server

@Composable
fun AssistantMessage(command: Command) {
    val colors = getColorsTheme()

    if (command.favName != null) {
        AsyncImage(
            model = "$server/${command.favName}",
            contentDescription = command.favName,
        )
    }
    Text(
        text = command.content,
        color = colors.TextColor,
        fontSize = 16.sp,
        modifier = Modifier.padding(vertical = 8.dp)
            .padding(all = 8.dp)
    )
}