package ui.componets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import domain.entity.TaskType
import ui.theme.getColorsTheme

@Composable
fun Sidebar(modifier: Modifier = Modifier, filterCommands: (taskType: TaskType?) -> Unit) {
    val colors = getColorsTheme()
    Column(modifier = modifier.padding(16.dp)) {
        Text(
            text = "All",
            color = colors.TextColor,
            fontSize = 18.sp,
            modifier = Modifier.fillMaxWidth().clickable {
                filterCommands(null)
            })
        Spacer(modifier = Modifier.height(16.dp))
        for (taskType in TaskType.entries) {
            if (taskType.show) {
                Text(
                    text = taskType.value,
                    color = colors.TextColor,
                    fontSize = 18.sp,
                    modifier = Modifier.fillMaxWidth().clickable {
                        filterCommands(taskType)
                    })
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}