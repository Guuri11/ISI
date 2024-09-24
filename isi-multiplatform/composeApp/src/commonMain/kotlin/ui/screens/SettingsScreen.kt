package ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import domain.entity.EnvironmentSetting
import domain.entity.GptSetting
import domain.entity.TaskType
import presentation.IsiUiState
import ui.componets.ErrorScreen
import ui.componets.LoadingScreen
import ui.theme.getColorsTheme
import kotlin.enums.EnumEntries

@Composable
fun Settings(
    uiState: IsiUiState,
    filterCommands: (taskType: TaskType?) -> Unit,
    onEnvironmentChange: (EnvironmentSetting) -> Unit,
    onGptChange: (GptSetting) -> Unit,
    goTo: (String) -> Unit,
) {
    val colors = getColorsTheme()

    when (uiState) {
        is IsiUiState.Loading -> {
            LoadingScreen(filterCommands, goTo)
        }

        is IsiUiState.Error -> {
            ErrorScreen(filterCommands, uiState.message, goTo)
        }

        is IsiUiState.Success -> {
            Box(modifier = Modifier.fillMaxSize()) {
                Row {
                    Column(
                        modifier = Modifier.background(colors.BackgroundColor).weight(3f).fillMaxHeight().padding(16.dp)
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
                                environment = uiState.enviroment,
                                onEnvironmentChange = {
                                    onEnvironmentChange(it)
                                }
                            )
                            LocalSetting(
                                gpt = uiState.gpt,
                                onGptChange = {
                                    onGptChange(it)
                                }
                            )
                        }
                    }
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
        Text(text = "Environment", fontSize = 20.sp, fontWeight = FontWeight.SemiBold, color = colors.TextColor)
        MultiSelectOptions(options = EnvironmentSetting.entries, selectedOption = selectedOption) { environment ->
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
    gpt: GptSetting,
    onGptChange: (GptSetting) -> Unit,
) {
    val colors = getColorsTheme()
    var selectedOption by remember { mutableStateOf<GptSetting?>(gpt) }
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Local Settings", fontSize = 20.sp, fontWeight = FontWeight.SemiBold, color = colors.TextColor)
        Text(text = "GPT Model", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = colors.TextColor)
        MultiSelectOptions(options = GptSetting.entries, selectedOption = selectedOption) { gpt ->
            if (gpt != null) {
                onGptChange(gpt)
            }
            selectedOption = gpt
        }
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