package ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import domain.entity.Chat
import domain.entity.MessageType
import domain.entity.TaskType
import kotlinx.datetime.toJavaLocalDateTime
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import presentation.LocalIsiViewModel
import ui.componets.*
import ui.componets.message.AssistantMessage
import ui.componets.message.UserMessage
import java.time.format.DateTimeFormatter

@Composable
fun IsiChatScreen() {
    val viewModel = LocalIsiViewModel.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var textState by remember { mutableStateOf(TextFieldValue()) }

    // used to mark messages by date
    val messageDate: MutableState<String?> = remember { mutableStateOf(null) }
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    val sendCommand: (String, Chat?) -> Unit = { prompt, chat ->
        viewModel.sendCommand(prompt, chat)
    }

    val chatListState = rememberLazyListState()

    LaunchedEffect(uiState.commands) {
        chatListState.animateScrollToItem(chatListState.layoutInfo.totalItemsCount)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            IsiKottie(size = if (uiState.commands.isEmpty()) 300.dp else 100.dp)
        }

        when {
            uiState.loading -> {
                Box(modifier = Modifier.weight(1f).fillMaxWidth().padding(16.dp)) {
                    LoadingText()
                }
            }

            !uiState.errorMessage.isNullOrEmpty() -> {
                Box(modifier = Modifier.weight(1f).fillMaxWidth().padding(16.dp)) {
                    ErrorText(uiState.errorMessage!!)
                }
            }

            else -> {
                if (uiState.commands.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(8.dp),
                        state = chatListState
                    ) {
                        items(uiState.commands) { command ->
                            // Formatea la fecha
                            val formattedDate = command.createdAt.toJavaLocalDateTime().format(formatter)

                            if (formattedDate != messageDate.value) {
                                messageDate.value = formattedDate
                                // Mostrar la fecha formateada
                                Text(
                                    text = "-------------$formattedDate-------------",
                                    modifier = Modifier.fillMaxWidth(),
                                    style = MaterialTheme.typography.bodySmall,
                                    textAlign = TextAlign.Center
                                )
                            }

                            if (command.messageType == MessageType.ASSISTANT) {
                                AssistantMessage(command)
                            } else {
                                Box(modifier = Modifier.fillMaxWidth()) {
                                    UserMessage(
                                        command, modifier = Modifier
                                            .padding(vertical = 8.dp)
                                            .background(
                                                color = MaterialTheme.colorScheme.secondary,
                                                shape = RoundedCornerShape(12)
                                            )
                                            .align(alignment = Alignment.CenterEnd)
                                            .padding(all = 8.dp)
                                    )
                                }
                            }
                        }
                    }
                }

            }
        }

        Column {
            TaskTypeSelector()
            Row(modifier = Modifier.fillMaxWidth()) {
                SendMessage {
                    if (it.isNotEmpty()) {
                        textState = TextFieldValue()
                        sendCommand(it, uiState.chat)
                    }
                }
            }
        }
    }
}
