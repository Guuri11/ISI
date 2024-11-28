package ui.componets.message

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import domain.entity.Command

@Composable
fun UserMessage(command: Command) {
    Box(modifier = Modifier.fillMaxWidth()) {
    Text(
        text = command.content,
        style = MaterialTheme.typography.bodyMedium.copy(
            color = MaterialTheme.colorScheme.onSecondary
        ),
        modifier = Modifier
            .padding(vertical = 8.dp)
            .background(
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(12)
            )
            .align(alignment = Alignment.CenterEnd)
            .padding(all = 8.dp),
    )
    }
}
