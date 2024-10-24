package presentation

import androidx.compose.runtime.staticCompositionLocalOf
import data.CommandRepositoryImpl
import data.CommandRepositoryLocalImpl
import domain.entity.*
import domain.mapper.createCommandFromString
import domain.repository.CommandRepository
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import java.net.ConnectException

val LocalIsiViewModel = staticCompositionLocalOf<IsiViewModel> {
    error("No IsiViewModel provided")
}

data class IsiUiState(
    val commands: List<Command> = emptyList(),
    val chat: Chat? = null,
    val taskTypeToFilter: TaskType? = null,
    val taskTypeToRequest: TaskType? = TaskType.OTHER_TOPICS,
    val enviroment: EnvironmentSetting? = EnvironmentSetting.LOCAL,
    val gpt: GptSetting = GptSetting.GPT_4O_MINI,
    val errorMessage: String? = null,
    val loading: Boolean = true,
)


class IsiViewModel(private var repo: CommandRepository, private val isLocal: Boolean) : ViewModel() {
    private val _uiState = MutableStateFlow<IsiUiState>(IsiUiState())
    private var allCommands = emptyList<Command>()
    private var currentChat: Chat? = null
    private var currentEnvironment: EnvironmentSetting? = null
    private var currentGpt: GptSetting = GptSetting.GPT_4O_MINI
    private var taskType: TaskType? = null

    val uiState = _uiState.asStateFlow()

    init {
        getAllCommands()
        currentEnvironment = if (isLocal) EnvironmentSetting.LOCAL else EnvironmentSetting.PRODUCTION
        _uiState.update { it.copy(loading = false) }
    }

    private fun getAllCommands(chat: Chat? = null) {
        viewModelScope.launch {
            try {
                Napier.i { "Getting all commands" }
                val commands = repo.findAll()
                allCommands = commands
                currentChat = chat
                _uiState.update {
                    it.copy(
                        commands
                    )
                }
            } catch (e: Exception) {
                // TODO: move error to enum or something like that
                Napier.e { "Error getting commands -> ${e.message}" }

                if (e is ConnectException && currentEnvironment == EnvironmentSetting.PRODUCTION) {
                    currentEnvironment = EnvironmentSetting.LOCAL
                    repo = CommandRepositoryLocalImpl(currentGpt)
                    getAllCommands()
                } else {
                    _uiState.update { it.copy(errorMessage = e.message ?: "Unknown error occurred") }
                }
            }
        }
    }

    fun setTaskType(taskType: TaskType?) {
        this.taskType = taskType
        _uiState.update {
            it.copy(
                taskTypeToRequest = taskType
            )
        }
    }

    fun filterCommands(taskTypeSelected: TaskType?) {
        viewModelScope.launch {
            try {
                Napier.i { "Filtering commands" }

                val filteredCommands = if (taskTypeSelected == null) {
                    allCommands
                } else {
                    allCommands.filter { it.task == taskTypeSelected }
                }

                _uiState.update {
                    it.copy(
                        commands = filteredCommands,
                        taskTypeToFilter = taskTypeSelected
                    )
                }
            } catch (e: Exception) {
                Napier.e { "Error filtering -> $e" }
                _uiState.update { it.copy(errorMessage = e.message ?: "Unknown error occurred") }
            }
        }
    }

    fun sendCommand(prompt: String, chat: Chat?) {
        viewModelScope.launch {
            try {
                Napier.i { "Sending command" }
                allCommands += createCommandFromString(content = prompt, messageType = MessageType.USER)
                val command = repo.create(allCommands, chat, taskType)
                Napier.i { "ISI response -> $command" }

                if (currentEnvironment?.equals(EnvironmentSetting.LOCAL) == true) {
                    updateMessagesLocal(command)
                } else {
                    getAllCommands(command.chat)
                }
            } catch (e: Exception) {
                Napier.e { "Error creating commands -> $e" }
                _uiState.update { it.copy(errorMessage = e.message ?: "Unknown error occurred") }
            }
        }
    }

    fun onEnvironmentChange(environment: EnvironmentSetting) {
        Napier.i { "New environment $environment" }
        repo = if (environment == EnvironmentSetting.LOCAL) {
            CommandRepositoryLocalImpl(currentGpt)
        } else {
            CommandRepositoryImpl()
        }
        currentEnvironment = environment
        getAllCommands()

        _uiState.update {
            it.copy(
                enviroment = environment
            )
        }
    }

    fun onGptChange(gpt: GptSetting) {
        Napier.i { "New gpt $gpt" }
        currentGpt = gpt
        repo = CommandRepositoryLocalImpl(currentGpt)

        _uiState.update {
            it.copy(
                gpt = gpt
            )
        }
    }

    private fun updateMessagesLocal(command: Command) {
        allCommands += command
        val taskTypeSelected = (_uiState.value as? IsiUiState)?.taskTypeToFilter
        val filteredCommands = if (taskTypeSelected == null) {
            allCommands
        } else {
            allCommands.filter { it.task == taskTypeSelected }
        }
        _uiState.update { it.copy(commands = filteredCommands, taskTypeToFilter = taskTypeSelected) }
    }
}