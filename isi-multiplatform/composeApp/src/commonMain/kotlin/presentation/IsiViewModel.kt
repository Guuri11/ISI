package presentation

import domain.entity.Chat
import domain.entity.Command
import domain.entity.MessageType
import domain.entity.TaskType
import domain.mapper.createCommandFromString
import domain.repository.CommandRepository
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

sealed class IsiUiState {
    data object Loading : IsiUiState()
    data class Success(
        val commands: List<Command>,
        val taskTypeSelected: TaskType? = null,
    ) : IsiUiState()

    data class Error(val message: String) : IsiUiState()
}


class IsiViewModel(private val repo: CommandRepository, private val isLocal: Boolean) : ViewModel() {
    private val _uiState = MutableStateFlow<IsiUiState>(IsiUiState.Loading)
    val uiState = _uiState.asStateFlow()
    private var allCommands = emptyList<Command>()

    init {
        getAllCommands()
    }

    private fun getAllCommands() {
        viewModelScope.launch {
            try {
                Napier.i { "Getting all commands" }
                val commands = repo.findAll()
                allCommands = commands
                Napier.i { "Command list -> $commands" }
                _uiState.value = IsiUiState.Success(commands)
            } catch (e: Exception) {
                // TODO: move error to enum or something like that
                Napier.e { "Error getting commands -> $e" }
                _uiState.value = IsiUiState.Error(e.message ?: "Unknown error occurred")
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

                _uiState.value = IsiUiState.Success(commands = filteredCommands, taskTypeSelected = taskTypeSelected)
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

                if (isLocal) {
                    updateMessagesLocal(command)
                } else {
                    getAllCommands()
                }
            } catch (e: Exception) {
                Napier.e { "Error creating commands -> $e" }
                _uiState.value = IsiUiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    private fun updateMessagesLocal(command: Command) {
        allCommands += command
        val taskTypeSelected = (_uiState.value as? IsiUiState.Success)?.taskTypeSelected
        val filteredCommands = if (taskTypeSelected == null) {
            allCommands
        } else {
            allCommands.filter { it.task == taskTypeSelected }
        }
        _uiState.value = IsiUiState.Success(commands = filteredCommands, taskTypeSelected = taskTypeSelected)
    }
}