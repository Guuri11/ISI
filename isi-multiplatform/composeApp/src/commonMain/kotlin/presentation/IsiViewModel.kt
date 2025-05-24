package presentation

import androidx.compose.runtime.staticCompositionLocalOf
import com.guuri11.isi.Settings
import data.repository.SettingsRepository
import data.service.storeCarCoordinatesService
import data.sources.Database
import dev.jordond.compass.geolocation.Geolocator
import domain.usecase.isLocalhostUseCase
import getGeoLocator
import getHttpClient
import getPlatform
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import openApp
import org.isi.data.repository.CommandRepositoryImpl
import org.isi.data.repository.CommandRepositoryLocalImpl
import org.isi.domain.mapper.createCommandFromString
import org.isi.domain.models.*
import org.isi.domain.repository.CommandRepository
import java.net.ConnectException
import java.util.*

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
    val environment: EnvironmentSetting? = EnvironmentSetting.LOCAL,
    val errorMessage: String? = null,
    val loading: Boolean = true,
    val settingsRepository: SettingsRepository = SettingsRepository(Database()),
    val geolocator: Geolocator = getGeoLocator()
)


class IsiViewModel() :
    ViewModel() {
    private var repo: CommandRepository
    private val _uiState = MutableStateFlow(IsiUiState())
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

        if (settings.wifis.isNullOrBlank() || isLocalhostUseCase(settings.wifis!!)) {
            currentEnvironment = EnvironmentSetting.LOCAL
            repo = CommandRepositoryLocalImpl(httpClient = getHttpClient())
        } else {
            currentEnvironment = EnvironmentSetting.PRODUCTION
            repo = CommandRepositoryImpl(settings.server, getHttpClient())
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
                    repo = CommandRepositoryLocalImpl(currentGpt, getHttpClient())
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

    fun handleCommand(prompt: String, chat: Chat?) {
        val lowerCaseCommand = prompt.lowercase(Locale.getDefault())
        when {
            getPlatform().name.contains("Android") && TaskType.SAVE_CAR_COORDINATES.options.contains(lowerCaseCommand) -> saveCarCoordinates(
                prompt
            )

            TaskType.OPEN_APP_CAMERA_VISION.matches(lowerCaseCommand) -> openApp(
                prompt,
                AppsAvailable.CAMERA_VISION
            )

            TaskType.OPEN_APP_YOUTUBE.matches(lowerCaseCommand) -> {
                val query = lowerCaseCommand.removePrefix("busca en youtube").trim().takeIf { it.isNotEmpty() }

                openApp(prompt, AppsAvailable.YOUTUBE, query)
            }

            TaskType.OPEN_APP_SPOTIFY.matches(lowerCaseCommand) -> {
                val query = lowerCaseCommand.removePrefix("busca en spotify").trim().takeIf { it.isNotEmpty() }

                openApp(prompt, AppsAvailable.SPOTIFY, query)
            }

            else -> sendCommand(prompt, chat)
        }
    }

    private fun openApp(prompt: String, app: AppsAvailable, args: String? = null) {
        val commandFromStringUser =
            createCommandFromString(content = prompt, messageType = MessageType.USER)
        updateCommandsList(commandFromStringUser)

        openApp(app, args.orEmpty())
    }

    fun saveCarCoordinates(prompt: String? = null) {
        viewModelScope.launch {
            try {
                storeCarCoordinatesService(
                    viewModel = this@IsiViewModel,
                    updateLocation = { location ->
                        _uiState.update { currentState ->
                            currentState.copy(
                                settings = currentState.settings.copy(
                                    carLatitude = location?.coordinates?.latitude,
                                    carLongitude = location?.coordinates?.longitude
                                )
                            )
                        }
                    },
                    updateStreet = { street ->
                        _uiState.update { currentState ->
                            currentState.copy(
                                settings = currentState.settings.copy(
                                    carStreet = street?.street
                                )
                            )
                        }
                    }
                )
                if (prompt != null) {
                    val commandFromStringUser =
                        createCommandFromString(content = prompt, messageType = MessageType.USER)
                    updateCommandsList(commandFromStringUser)

                    val commandFromStringAssistant = createCommandFromString(
                        content = "Ubicación guardada en ${uiState.value.settings.carStreet}",
                        messageType = MessageType.ASSISTANT
                    )

                    updateCommandsList(commandFromStringAssistant)
                }
            } catch (e: Exception) {
                Napier.e { "Error saving car coordinates: ${e.message}" }
                _uiState.update { currentState ->
                    currentState.copy(
                        errorMessage = "Error saving car coordinates: ${e.message}"
                    )
                }
            }
        }
    }

    private fun sendCommand(prompt: String, chat: Chat?) {
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
        allCommands += command
        if (currentEnvironment?.equals(EnvironmentSetting.LOCAL) == true) {
            updateMessagesLocal(command)
        } else {
            getAllCommands(command.chat)
        }
    }

    fun onEnvironmentChange(environment: EnvironmentSetting) {
        Napier.i { "New environment $environment" }
        repo = if (environment == EnvironmentSetting.LOCAL) {
            CommandRepositoryLocalImpl(currentGpt, getHttpClient())
        } else {
            CommandRepositoryImpl(settings.server, getHttpClient())
        }
        currentEnvironment = environment
        getAllCommands()

        _uiState.update {
            it.copy(
                environment = environment
            )
        }
    }

    fun onSettingsChange(newSettings: Settings) {
        Napier.i { "New settings key $settings" }
        val gptSetting = GptSetting.fromValue(newSettings.modelAI)
        settings = newSettings
        currentGpt = gptSetting
        repo = CommandRepositoryLocalImpl(gptSetting, getHttpClient())
        settingsRepository.save(newSettings)

        _uiState.update {
            it.copy(
                settings = settings
            )
        }
    }

    private fun updateMessagesLocal(command: Command) {
        val taskTypeSelected = (_uiState.value as? IsiUiState)?.taskTypeToFilter
        val filteredCommands = if (taskTypeSelected == null) {
            allCommands
        } else {
            allCommands.filter { it.task == taskTypeSelected }
        }
        _uiState.update { it.copy(commands = filteredCommands, taskTypeToFilter = taskTypeSelected) }
    }
}