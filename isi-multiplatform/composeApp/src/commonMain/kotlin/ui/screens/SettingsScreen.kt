package ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.guuri11.isi.Settings
import domain.entity.EnvironmentSetting
import domain.entity.GptSetting
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import presentation.LocalIsiViewModel
import kotlin.enums.EnumEntries

@Composable
fun SettingsScreen() {

    val viewModel = LocalIsiViewModel.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val onEnvironmentChange: (EnvironmentSetting) -> Unit = { env ->
        viewModel.onEnvironmentChange(env)
    }

    val onWifiChange: (String) -> Unit = { wifis ->
        viewModel.onSettingsChange(uiState.settings.copy(wifis = wifis))
    }

    val onSettingChange: (Settings) -> Unit = { settings ->
        viewModel.onSettingsChange(settings)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {
        if (uiState.enviroment != null) {
            EnvironmentSetting(
                environment = uiState.enviroment!!,
                onEnvironmentChange = {
                    onEnvironmentChange(it)
                }
            )
            LocalSetting(
                settings = uiState.settings,
                onSettingChange = {
                    onSettingChange(it)
                }
            )
            AIModelKeySetting(
                settings = uiState.settings,
                onSettingChange = {
                    onSettingChange(it)
                }
            )
            WifisSetting(
                settings = uiState.settings,
                onSettingChange = {
                    onWifiChange(it)
                }
            )
            ServerSetting(
                settings = uiState.settings,
                onSettingChange = {
                    onSettingChange(it)
                }
            )
        }
    }
}

@Composable
fun EnvironmentSetting(
    environment: EnvironmentSetting,
    onEnvironmentChange: (EnvironmentSetting) -> Unit,
) {
    var selectedOption by remember { mutableStateOf<EnvironmentSetting?>(environment) }
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Environment",
            style = MaterialTheme.typography.titleMedium
        )
        MultiSelectOptions(
            options = EnvironmentSetting.entries,
            selectedOption = selectedOption
        ) { environment ->
            if (environment != null) {
                onEnvironmentChange(environment)
            }

            selectedOption = environment
        }
        HorizontalDivider(thickness = 2.dp, modifier = Modifier.padding(vertical = 16.dp).padding(bottom = 16.dp))
    }
}

@Composable
fun LocalSetting(
    settings: Settings,
    onSettingChange: (Settings) -> Unit,
) {
    var selectedOption by remember { mutableStateOf<GptSetting?>(GptSetting.fromValue(settings.modelAI)) }
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "AI Model",
            style = MaterialTheme.typography.titleMedium
        )
        MultiSelectOptions(options = GptSetting.entries, selectedOption = selectedOption) { gpt ->
            if (gpt != null) {
                onSettingChange(settings.copy(modelAI = gpt.value))
            }
            selectedOption = gpt
        }
        HorizontalDivider(thickness = 2.dp, modifier = Modifier.padding(vertical = 16.dp).padding(bottom = 16.dp))
    }
}

@Composable
fun AIModelKeySetting(
    settings: Settings,
    onSettingChange: (Settings) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "AI Model API Key",
            style = MaterialTheme.typography.titleMedium
        )
        TextField(
            value = settings.modelAIApiKey,
            onValueChange = {
                onSettingChange(
                    settings.copy(
                        modelAIApiKey = it
                    )
                )
            }
        )
        HorizontalDivider(thickness = 2.dp, modifier = Modifier.padding(vertical = 16.dp).padding(bottom = 16.dp))
    }
}

@Composable
fun WifisSetting(
    settings: Settings,
    onSettingChange: (String) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "WiFi List",
            style = MaterialTheme.typography.titleMedium
        )
        TextField(
            value = if (settings.wifis.isNullOrBlank()) "" else settings.wifis,
            onValueChange = {
                onSettingChange(it)
            },
        )
        HorizontalDivider(thickness = 2.dp, modifier = Modifier.padding(vertical = 16.dp).padding(bottom = 16.dp))
    }
}

@Composable
fun ServerSetting(
    settings: Settings,
    onSettingChange: (Settings) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Server",
            style = MaterialTheme.typography.titleMedium
        )
        TextField(
            value = settings.server,
            onValueChange = {
                onSettingChange(
                    settings.copy(
                        server = it
                    )
                )
            }
        )
        /** TODO: add divider if a new setting is created **/
    }
}


@Composable
fun <T> MultiSelectOptions(
    options: EnumEntries<T>,
    selectedOption: T?,
    onSelectionChange: (T?) -> Unit,
) where T : Enum<T> {
    Column(modifier = Modifier.padding(16.dp)) {
        options.forEach { option ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = selectedOption == option,
                    onCheckedChange = { isChecked ->
                        if (isChecked) {
                            onSelectionChange(option)
                        } else {
                            onSelectionChange(null)
                        }
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(option.name)
            }
        }
    }
}