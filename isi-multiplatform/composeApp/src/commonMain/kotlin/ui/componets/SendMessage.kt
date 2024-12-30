package ui.componets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import platform.createSpeechToText
import getPlatform

@Composable
fun SendMessage(sendMessage: (String) -> Unit) {
    var inputText by remember { mutableStateOf("") }
    // State for recognized speech
    val speechToText = createSpeechToText() // Your platform-specific implementation

    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        TextField(
            modifier = Modifier
                .padding(10.dp)
                .weight(1f),
            value = inputText,
            placeholder = {
                Text("Type message...")
            },
            onValueChange = {
                inputText = it
            },
            trailingIcon = {
                if (inputText.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .clickable {
                                sendMessage(inputText)
                                inputText = ""
                            }
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        )

        if (getPlatform().name.startsWith("Android")) {
            IconButton(
                onClick = {
                speechToText.startListening { result ->
                    inputText = result
                }
            }) {
                Icon(
                    imageVector = Icons.Filled.Mic,
                    contentDescription = "Mic",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}