package ui.componets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import domain.entity.TaskType
import ui.theme.getColorsTheme

@Composable
fun TaskTypeSelector() {
    val colors = getColorsTheme()
    val showList: MutableState<Boolean> = remember { mutableStateOf(false) }
    val taskType: MutableState<TaskType> = remember { mutableStateOf(TaskType.OTHER_TOPICS) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            "Task Type: ${taskType.value.value}",
            color = colors.TextColor,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            modifier = Modifier.clickable {
                showList.value = true
            }
        )

        if (showList.value) {
            TaskType.entries.map {
                Text(
                    it.value,
                    color = colors.TextColor,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    modifier = Modifier.clickable {
                        taskType.value = it
                        showList.value = false
                    }
                )
            }
        }
    }
}