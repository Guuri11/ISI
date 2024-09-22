package ui.componets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import domain.entity.TaskType
import getPlatform
import ui.theme.getColorsTheme

@Composable
fun LoadingScreen(filterCommands: (taskType: TaskType?) -> Unit, goTo: (String) -> Unit) {
    val colors = getColorsTheme()
    val isExpanded = remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Row {
            if (isExpanded.value || !getPlatform().name.startsWith("Android")) {
                Sidebar(
                    modifier = Modifier.weight(if (getPlatform().name.startsWith("Android")) 3f else 1f).fillMaxHeight()
                        .background(Color(0xFF171717))
                        .padding(top = if (getPlatform().name.startsWith("Android")) 80.dp else 0.dp),
                    filterCommands = filterCommands,
                    goTo = goTo
                )
            }
            Column(
                modifier = Modifier.background(colors.BackgroundColor).weight(3f).fillMaxHeight().padding(16.dp)
            ) {
                // Logo centered
                Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                    IsiKottie(size = 500.dp)
                }

                // Loading Text
                Box(modifier = Modifier.weight(1f).fillMaxWidth().padding(16.dp)) {
                    Text(
                        "Loading data...",
                        color = colors.TextColor,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                    )
                }
                // Text input box
                Row(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                    SendMessage { }
                }
            }
        }

        // Button for mobile device to expand sidebar
        AndroidFloatingButton(
            isExpanded = isExpanded,
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .padding(16.dp)
        )
    }
}