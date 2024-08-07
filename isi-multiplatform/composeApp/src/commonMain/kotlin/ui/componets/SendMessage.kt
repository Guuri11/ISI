package ui.componets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import ui.theme.getColorsTheme

@Composable
fun SendMessage(sendMessage: (String) -> Unit) {
    val colors = getColorsTheme()
    var inputText by remember { mutableStateOf("") }
    TextField(
        modifier = Modifier.fillMaxWidth()
            .background(Color(0xFF2F2F2F), shape = RoundedCornerShape(24.dp))
            .padding(10.dp),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color(0xFF2F2F2F),
            placeholderColor = colors.TextColor,
            cursorColor = colors.TextColor,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        ),
        textStyle = TextStyle(color = colors.TextColor),
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
                        tint = colors.Purple
                    )
                }
            }
        }
    )
}