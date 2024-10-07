package ui.componets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import domain.entity.TaskType
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import presentation.LocalIsiViewModel
import ui.theme.getColorsTheme

@Composable
fun TaskTypeSelector() {
    val viewModel = LocalIsiViewModel.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val state = uiState

    val colors = getColorsTheme()
    val showList: MutableState<Boolean> = remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            "Task Type: ${state.taskTypeToRequest?.value}",
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
                        viewModel.setTaskType(it)
                        showList.value = false
                    }
                )
            }
        }
    }
}