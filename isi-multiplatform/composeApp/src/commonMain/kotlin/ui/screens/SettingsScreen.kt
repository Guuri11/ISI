package ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guuri11.isi.Settings
import domain.entity.EnvironmentSetting
import domain.entity.GptSetting
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import presentation.LocalIsiViewModel
import kotlin.enums.EnumEntries

@Composable
fun SettingsScreen(goTo: (String) -> Unit) {

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

    Box(modifier = Modifier.fillMaxSize()) {
        Row {
            Column(
                modifier = Modifier.weight(3f).fillMaxHeight()
                    .padding(16.dp)
            ) {
                if (uiState.enviroment != null) {
                    IconButton(onClick = {
                        goTo("/home")
                    }) {
                        Icon(
                            modifier = Modifier.padding(start = 16.dp),
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
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
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
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
        Divider(thickness = 2.dp)
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
            text = "Local Settings",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
        )
        Text(
            text = "GPT Model",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
        )
        MultiSelectOptions(options = GptSetting.entries, selectedOption = selectedOption) { gpt ->
            if (gpt != null) {
                onSettingChange(settings.copy(modelAI = gpt.value))
            }
            selectedOption = gpt
        }
        Divider(thickness = 2.dp)
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
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
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
        Divider(thickness = 2.dp)
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
            text = "List of Wifis splitted by ++",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
        )
        TextField(
            value = if (settings.wifis.isNullOrBlank()) "" else settings.wifis,
            onValueChange = {
                onSettingChange(it)
            },
        )
        Divider(thickness = 2.dp)
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
            text = "Server for production",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
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
        Divider(thickness = 2.dp)
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