package presentation

import androidx.compose.runtime.staticCompositionLocalOf
import com.guuri11.isi.Settings
import data.repository.CommandRepositoryImpl
import data.repository.CommandRepositoryLocalImpl
import data.sources.Database
import data.repository.SettingsRepository
import domain.entity.Chat
import domain.entity.Command
import domain.entity.EnvironmentSetting
import domain.entity.GptSetting
import domain.entity.MessageType
import domain.entity.TaskType
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
import utils.isLocal

val LocalIsiViewModel = staticCompositionLocalOf<IsiViewModel> {
    error("No IsiViewModel provided")
}

data class IsiUiState(
    val settings: Settings = Settings(
        id = 1,
        modelAI = GptSetting.GPT_4O_MINI.value,
        modelAIApiKey = "",
        wifis = "",
        carLatitude = null,
        carLongitude = null,
        carStreet = null,
        server = "http://192.168.1.76:8080",
        showOnboarding = 1,
    ),
    val commands: List<Command> = emptyList(),
    val chat: Chat? = null,
    val taskTypeToFilter: TaskType? = null,
    val taskTypeToRequest: TaskType? = TaskType.OTHER_TOPICS,
    val enviroment: EnvironmentSetting? = EnvironmentSetting.LOCAL,
    val errorMessage: String? = null,
    val loading: Boolean = true,
    val settingsRepository: SettingsRepository = SettingsRepository(Database())
)


class IsiViewModel() :
    ViewModel() {
    private var repo: CommandRepository
    private val _uiState = MutableStateFlow<IsiUiState>(IsiUiState())
    private var settings: Settings
    private var allCommands = emptyList<Command>()
    private var currentChat: Chat? = null
    private var currentEnvironment: EnvironmentSetting? = null
    private var currentGpt: GptSetting
    private var taskType: TaskType? = null
    private var settingsRepository: SettingsRepository = SettingsRepository(Database())

    val uiState = _uiState.asStateFlow()

    init {
        settings = settingsRepository.get()
        currentGpt = GptSetting.fromValue(settings.modelAI)

        _uiState.update {
            it.copy(
                loading = false,
                settings = settings,
            )
        }

        if (settings.wifis.isNullOrBlank() || isLocal(settings.wifis!!)) {
            currentEnvironment = EnvironmentSetting.LOCAL
            repo = CommandRepositoryLocalImpl()
        } else {
            currentEnvironment = EnvironmentSetting.PRODUCTION
            repo = CommandRepositoryImpl(settings.server)
        }

        getAllCommands()
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
                        commands = commands
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
                    _uiState.update {
                        it.copy(
                            errorMessage = e.message ?: "Unknown error occurred"
                        )
                    }
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

    fun sendCommand(prompt: String, chat: Chat?) {
        _uiState.update { it.copy(errorMessage = "") }
        viewModelScope.launch {
            try {
                Napier.i { "Sending command" }
                val commandFromString = createCommandFromString(content = prompt, messageType = MessageType.USER)
                updateCommandsList(commandFromString)
                val command = repo.create(allCommands, chat, taskType, settings.modelAIApiKey)
                Napier.i { "ISI response -> $command" }

                updateCommandsList(command)
            } catch (e: Exception) {
                Napier.e { "Error creating commands -> $e" }
                _uiState.update { it.copy(errorMessage = e.message ?: "Unknown error occurred") }
            }
        }
    }

    private fun updateCommandsList(command: Command) {
        if (currentEnvironment?.equals(EnvironmentSetting.LOCAL) == true) {
            updateMessagesLocal(command)
        } else {
            getAllCommands(command.chat)
        }
    }

    fun onEnvironmentChange(environment: EnvironmentSetting) {
        Napier.i { "New environment $environment" }
        repo = if (environment == EnvironmentSetting.LOCAL) {
            CommandRepositoryLocalImpl(currentGpt)
        } else {
            CommandRepositoryImpl(settings.server)
        }
        currentEnvironment = environment
        getAllCommands()

        _uiState.update {
            it.copy(
                enviroment = environment
            )
        }
    }

    fun onSettingsChange(newSettings: Settings) {
        Napier.i { "New settings key $settings" }
        val gptSetting = GptSetting.fromValue(newSettings.modelAI)
        settings = newSettings
        currentGpt = gptSetting
        repo = CommandRepositoryLocalImpl(gptSetting)
        settingsRepository.save(newSettings)

        _uiState.update {
            it.copy(
                settings = settings
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