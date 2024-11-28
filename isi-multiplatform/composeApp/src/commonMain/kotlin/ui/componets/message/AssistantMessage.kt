package ui.componets.message

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.mikepenz.markdown.compose.components.markdownComponents
import com.mikepenz.markdown.compose.elements.highlightedCodeBlock
import com.mikepenz.markdown.compose.elements.highlightedCodeFence
import com.mikepenz.markdown.m3.Markdown
import domain.entity.Command
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import presentation.LocalIsiViewModel
import ui.componets.FullScreenImageDialog

@Composable
fun AssistantMessage(command: Command) {
    val viewModel = LocalIsiViewModel.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showDialog by remember { mutableStateOf(false) }

    // Show the image in fullscreen when clicked
    if (showDialog) {
        FullScreenImageDialog(
            imageUrl = "${uiState.settings.server}/static/${command.favName}",
            onDismiss = { showDialog = false }
        )
    }

    // Main content
    Column {
        if (command.favName != null) {
            AsyncImage(
                model = "${uiState.settings.server}/static/${command.favName}",
                contentDescription = command.favName,
                modifier = Modifier
                    .clickable { showDialog = true }
            )
        }
        SelectionContainer {
            Markdown(
                command.content,
                components = markdownComponents(
                    codeBlock = highlightedCodeBlock,
                    codeFence = highlightedCodeFence,
                ),
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceContainerHigh,
                        shape = RoundedCornerShape(12)
                    )
                    .padding(all = 12.dp)
            )
        }
    }
}