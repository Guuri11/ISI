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
import ui.theme.getColorsTheme
import kotlin.enums.EnumEntries

@Composable
fun SettingsScreen(goTo: (String) -> Unit) {

    val viewModel = LocalIsiViewModel.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val colors = getColorsTheme()

    val onEnvironmentChange: (EnvironmentSetting) -> Unit = { env ->
        viewModel.onEnvironmentChange(env)
    }

    val onGptChange: (Settings) -> Unit = { settings ->
        viewModel.onGptChange(GptSetting.fromValue(settings.modelAI))
    }

    val onApiKeyChange: (Settings) -> Unit = { settings ->
        viewModel.onApiKeyChange(settings.modelAIApiKey)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Row {
            Column(
                modifier = Modifier.background(colors.BackgroundColor).weight(3f).fillMaxHeight()
                    .padding(16.dp)
            ) {
                if (uiState.enviroment != null) {
                    IconButton(onClick = {
                        goTo("/home")
                    }) {
                        Icon(
                            modifier = Modifier.padding(start = 16.dp),
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            tint = colors.TextColor,
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
                            onGptChange(it)
                        }
                    )
                    AIModelKeySetting(
                        settings = uiState.settings,
                        onSettingChange = {
                            onApiKeyChange(it)
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
    val colors = getColorsTheme()
    var selectedOption by remember { mutableStateOf<EnvironmentSetting?>(environment) }
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Environment",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = colors.TextColor
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
        Divider(color = colors.TextColor, thickness = 2.dp)
    }
}

@Composable
fun LocalSetting(
    settings: Settings,
    onSettingChange: (Settings) -> Unit,
) {
    val colors = getColorsTheme()
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
            color = colors.TextColor
        )
        Text(
            text = "GPT Model",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = colors.TextColor
        )
        MultiSelectOptions(options = GptSetting.entries, selectedOption = selectedOption) { gpt ->
            if (gpt != null) {
                onSettingChange(settings.copy(modelAI = gpt.value))
            }
            selectedOption = gpt
        }
        Divider(color = colors.TextColor, thickness = 2.dp)
    }
}

@Composable
fun AIModelKeySetting(
    settings: Settings,
    onSettingChange: (Settings) -> Unit,
) {
    val colors = getColorsTheme()
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "AI Model API Key",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = colors.TextColor
        )
        TextField(
            value = settings.modelAIApiKey,
            onValueChange = {
                onSettingChange(
                    settings.copy(
                        modelAIApiKey = it
                    )
                )
            },
            textStyle = TextStyle(color = colors.TextColor),
        )
        Divider(color = colors.TextColor, thickness = 2.dp)
    }
}

@Composable
fun <T> MultiSelectOptions(
    options: EnumEntries<T>,
    selectedOption: T?,
    onSelectionChange: (T?) -> Unit,
) where T : Enum<T> {
    val colors = getColorsTheme()
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
                    },
                    colors = CheckboxDefaults.colors(
                        uncheckedColor = colors.TextColor

                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(option.name, color = colors.TextColor)
            }
        }
    }
}