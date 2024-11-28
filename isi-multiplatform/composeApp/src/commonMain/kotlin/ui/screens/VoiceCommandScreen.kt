package ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import domain.entity.Chat
import domain.entity.MessageType
import kotlinx.datetime.toJavaLocalDateTime
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import presentation.LocalIsiViewModel
import ui.componets.ErrorText
import ui.componets.IsiKottie
import ui.componets.LoadingText
import ui.componets.VoiceCommandInput
import ui.componets.message.AssistantMessage
import ui.componets.message.UserMessage
import java.time.format.DateTimeFormatter

@Composable
fun VoiceCommandScreen(goTo: (String) -> Unit) {
    val viewModel = LocalIsiViewModel.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var textState by remember { mutableStateOf(TextFieldValue()) }

    // used to mark messages by date
    val messageDate: MutableState<String?> = remember { mutableStateOf(null) }
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    val sendCommand: (String, Chat?) -> Unit = { prompt, chat ->
        viewModel.sendCommand(prompt, chat)
    }

    LaunchedEffect(Unit) {
        viewModel.setTaskType(null)
    }


    val chatListState = rememberLazyListState()

    LaunchedEffect(uiState.commands) {
        chatListState.animateScrollToItem(chatListState.layoutInfo.totalItemsCount)
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Row {
            Column(
                modifier = Modifier.weight(3f).fillMaxHeight().padding(16.dp)
            ) {
                // Logo centered
                Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
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
                        // Chat messages box
                        if (uiState.commands.isNotEmpty()) {
                            Box(modifier = Modifier.weight(8f).fillMaxWidth().padding(16.dp)) {
                                LazyColumn(modifier = Modifier.fillMaxWidth().padding(16.dp), state = chatListState) {
                                    items(uiState.commands) { command ->
                                        // Format the date
                                        val formattedDate = command.createdAt.toJavaLocalDateTime().format(formatter)

                                        // Check if the date is different from the current message date
                                        if (formattedDate != messageDate.value) {
                                            messageDate.value = formattedDate
                                            // Display formatted date TODO: replace lines by divider
                                            Text(
                                                text = "-------------$formattedDate-------------",
                                                modifier = Modifier.fillMaxWidth(),
                                                fontSize = 12.sp,
                                                textAlign = TextAlign.Center
                                            )
                                        }

                                        // Show message based on role
                                        if (command.messageType == MessageType.ASSISTANT) {
                                            AssistantMessage(command)
                                        } else {
                                            UserMessage(command)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                // Text input box
                VoiceCommandInput { it ->
                    if (it.isNotEmpty()) {
                        textState = TextFieldValue()
                        sendCommand(it, uiState.chat)
                    }
                }
            }


        }
    }
}