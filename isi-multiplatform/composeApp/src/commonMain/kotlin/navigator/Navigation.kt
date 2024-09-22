package navigator

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import data.CommandRepositoryImpl
import data.CommandRepositoryLocalImpl
import domain.entity.Chat
import domain.entity.EnvironmentSetting
import domain.entity.TaskType
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.viewmodel.viewModel
import presentation.IsiViewModel
import ui.screens.Home
import ui.screens.Settings
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

    val onEnvironmentChange: (environment: EnvironmentSetting) -> Unit = { e: EnvironmentSetting ->
        viewModel.onEnvironmentChange(environment = e)
    }

    val goTo: (path: String) -> Unit = { p: String ->
        navigator.navigate(p)
    }

    NavHost(
        navigator = navigator,
        initialRoute = "/home"
    ) {
        scene(route = "/home") {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            Home(uiState = uiState, filterCommands = filterCommands, sendCommand = sendCommand, goTo = goTo)
        }
        scene(route = "/settings") {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            Settings(uiState = uiState, filterCommands = filterCommands, onEnvironmentChange = onEnvironmentChange, goTo = goTo)
        }
    }
}