package navigator

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import data.CommandRepositoryImpl
import data.CommandRepositoryLocalImpl
import domain.entity.Chat
import domain.entity.TaskType
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.viewmodel.viewModel
import presentation.IsiViewModel
import ui.screens.Home
import utils.isLocal

@Composable
fun Navigation(navigator: Navigator) {

    val viewModel = viewModel(modelClass = (IsiViewModel::class)) {
        IsiViewModel(if (isLocal) CommandRepositoryLocalImpl() else CommandRepositoryImpl(), isLocal)
    }

    val filterCommands: (taskType: TaskType?) -> Unit = {
        viewModel.filterCommands(taskTypeSelected = it)
    }

    val sendCommand: (prompt: String, chat: Chat?) -> Unit = { s: String, chat: Chat? ->
        viewModel.sendCommand(s, chat)
    }

    NavHost(
        navigator = navigator,
        initialRoute = "/home"
    ) {
        scene(route = "/home") {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            Home(uiState = uiState, filterCommands = filterCommands, sendCommand = sendCommand)
        }
    }
}