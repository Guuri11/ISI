package ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import domain.entity.Chat
import domain.entity.MessageType
import domain.entity.TaskType
import kotlinx.datetime.toJavaLocalDateTime
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import presentation.LocalIsiViewModel
import ui.componets.ErrorText
import ui.componets.IsiKottie
import ui.componets.LoadingText
import ui.componets.VoiceCommandInput
import ui.componets.message.AssistantMessage
import ui.componets.message.UserMessage
import ui.theme.getColorsTheme
import java.time.format.DateTimeFormatter

@Composable
fun VoiceCommandScreen(goTo: (String) -> Unit) {
    val viewModel = LocalIsiViewModel.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val colors = getColorsTheme()
    var textState by remember { mutableStateOf(TextFieldValue()) }

    // used to mark messages by date
    val messageDate: MutableState<String?> = remember { mutableStateOf(null) }
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    val filterCommands: (TaskType?) -> Unit = {
        viewModel.filterCommands(it)
    }

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
                    // TODO: If there is not messages, make isi bigger. If there are, keep this size. Check if it is possible
                    // to make it with a animation
                    IsiKottie(size = if (uiState.commands.isEmpty()) 500.dp else 300.dp)
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
                                                color = colors.TextColor,
                                                textAlign = TextAlign.Center
                                            )
                                        }

                                        // Show message based on role
                                        if (command.messageType == MessageType.ASSISTANT) {
                                            AssistantMessage(command)
                                        } else {
                                            Box(modifier = Modifier.fillMaxWidth()) {
                                                UserMessage(
                                                    command, modifier = Modifier
                                                        .padding(vertical = 8.dp)
                                                        .background(
                                                            color = colors.Purple,
                                                            shape = RoundedCornerShape(24)
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