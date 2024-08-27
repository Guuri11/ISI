package ui.componets.message

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import domain.entity.Command
import ui.componets.FullScreenImageDialog
import ui.theme.getColorsTheme
import utils.static

@Composable
fun AssistantMessage(command: Command) {
    val colors = getColorsTheme()
    var showDialog by remember { mutableStateOf(false) }

    // Show the image in fullscreen when clicked
    if (showDialog) {
        FullScreenImageDialog(
            imageUrl = "$static/${command.favName}",
            onDismiss = { showDialog = false }
        )
    }

    // Main content
    Column {
        if (command.favName != null) {
            AsyncImage(
                model = "$static/${command.favName}",
                contentDescription = command.favName,
                modifier = Modifier
                    .clickable { showDialog = true }
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
}