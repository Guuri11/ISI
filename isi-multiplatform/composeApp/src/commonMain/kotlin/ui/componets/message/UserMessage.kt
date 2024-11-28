package ui.componets.message

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import domain.entity.Command

@Composable
fun UserMessage(command: Command, modifier: Modifier) {
    Text(
        text = command.content,
        style = MaterialTheme.typography.bodyMedium.copy(
            color = MaterialTheme.colorScheme.onSecondary
        ),
        modifier = modifier,
    )
}