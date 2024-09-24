package presentation

import data.CommandRepositoryImpl
import data.CommandRepositoryLocalImpl
import domain.entity.*
import domain.mapper.createCommandFromString
import domain.repository.CommandRepository
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import java.net.ConnectException

sealed class IsiUiState {
    data object Loading : IsiUiState()
    data class Success(
        val commands: List<Command>,
        val chat: Chat? = null,
        val taskTypeSelected: TaskType? = null,
        val enviroment: EnvironmentSetting? = EnvironmentSetting.LOCAL,
        val gpt: GptSetting = GptSetting.GPT_4O_MINI,
    ) : IsiUiState()

    data class Error(val message: String) : IsiUiState()
}


class IsiViewModel(private var repo: CommandRepository, private val isLocal: Boolean) : ViewModel() {
    private val _uiState = MutableStateFlow<IsiUiState>(IsiUiState.Loading)
    private var allCommands = emptyList<Command>()
    private var currentChat: Chat? = null
    private var currentEnvironment: EnvironmentSetting? = null
    private var currentGpt: GptSetting = GptSetting.GPT_4O_MINI

    val uiState = _uiState.asStateFlow()

    init {
        getAllCommands()
        currentEnvironment = if (isLocal) EnvironmentSetting.LOCAL else EnvironmentSetting.PRODUCTION
    }

    private fun getAllCommands(chat: Chat? = null) {
        viewModelScope.launch {
            try {
                Napier.i { "Getting all commands" }
                val commands = repo.findAll()
                allCommands = commands
                currentChat = chat
                Napier.i { "Command list -> $commands" }
                _uiState.value = IsiUiState.Success(commands, chat, enviroment = currentEnvironment)
            } catch (e: Exception) {
                // TODO: move error to enum or something like that
                Napier.e { "Error getting commands -> ${e.message}" }

                if (e is ConnectException && currentEnvironment == EnvironmentSetting.PRODUCTION) {
                    currentEnvironment = EnvironmentSetting.LOCAL
                    repo = CommandRepositoryLocalImpl(currentGpt)
                    getAllCommands()
                } else {
                    _uiState.value = IsiUiState.Error(e.message ?: "Unknown error occurred")
                }
            }
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

                _uiState.value = IsiUiState.Success(
                    commands = filteredCommands,
                    enviroment = currentEnvironment,
                    taskTypeSelected = taskTypeSelected
                )
            } catch (e: Exception) {
                Napier.e { "Error filtering -> $e" }
                _uiState.value = IsiUiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun sendCommand(prompt: String, chat: Chat?) {
        viewModelScope.launch {
            try {
                Napier.i { "Sending command" }
                allCommands += createCommandFromString(content = prompt, messageType = MessageType.USER)
                val command = repo.create(allCommands, chat)
                Napier.i { "ISI response -> $command" }

                if (currentEnvironment?.equals(EnvironmentSetting.LOCAL) == true) {
                    updateMessagesLocal(command)
                } else {
                    getAllCommands(command.chat)
                }
            } catch (e: Exception) {
                Napier.e { "Error creating commands -> $e" }
                _uiState.value = IsiUiState.Error(e.message ?: "Unknown error occurred")
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

        _uiState.value = IsiUiState.Success(
            commands = allCommands,
            enviroment = environment,
            chat = currentChat,
            taskTypeSelected = null
        )
    }

    fun onGptChange(gpt: GptSetting) {
        Napier.i { "New gpt $gpt" }
        currentGpt = gpt
        repo = CommandRepositoryLocalImpl(currentGpt)

        _uiState.value = IsiUiState.Success(
            commands = allCommands,
            enviroment = currentEnvironment,
            gpt = gpt,
            chat = currentChat,
            taskTypeSelected = null
        )
    }

    private fun updateMessagesLocal(command: Command) {
        allCommands += command
        val taskTypeSelected = (_uiState.value as? IsiUiState.Success)?.taskTypeSelected
        val filteredCommands = if (taskTypeSelected == null) {
            allCommands
        } else {
            allCommands.filter { it.task == taskTypeSelected }
        }
        _uiState.value = IsiUiState.Success(
            commands = filteredCommands,
            enviroment = currentEnvironment,
            taskTypeSelected = taskTypeSelected
        )
    }
}