package ui.componets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.isi.domain.models.TaskType
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import presentation.LocalIsiViewModel

@Composable
fun TaskTypeSelector() {
    val viewModel = LocalIsiViewModel.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val state = uiState

    val showList: MutableState<Boolean> = remember { mutableStateOf(false) }

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 8.dp)
    ) {
        Button(
            onClick = {
                showList.value = true
            },
            modifier = Modifier.align(alignment = Alignment.Start),
        ) {
            Text(
                "Task Type: ${state.taskTypeToRequest?.value}",
                style = MaterialTheme.typography.bodyLarge,
            )
        }

        if (showList.value) {
            TaskType.entries.map {
                Text(
                    it.value,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.clickable {
                        viewModel.setTaskType(it)
                        showList.value = false
                    }
                )
            }
        }
    }
}