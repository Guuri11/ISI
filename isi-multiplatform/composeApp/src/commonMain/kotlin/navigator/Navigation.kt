package navigator

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import data.CommandRepositoryImpl
import data.CommandRepositoryLocalImpl
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.viewmodel.viewModel
import presentation.IsiViewModel
import presentation.LocalIsiViewModel
import ui.screens.CarCoordsScreen
import ui.screens.HomeScreen
import ui.screens.SettingsScreen
import ui.screens.VoiceCommandScreen
import utils.IntentSpeechToText
import utils.isLocal

@Composable
fun Navigation(navigator: Navigator, intentSpeechToText: IntentSpeechToText? = null) {
    val viewModel = viewModel(modelClass = IsiViewModel::class) {
        IsiViewModel(if (isLocal) CommandRepositoryLocalImpl() else CommandRepositoryImpl(), isLocal)
    }

    val initialRoute: String =
        if (intentSpeechToText != null && intentSpeechToText.equals(IntentSpeechToText.ACTION)) "/voice-command" else "/home"

    CompositionLocalProvider(LocalIsiViewModel provides viewModel) {
        NavHost(
            navigator = navigator,
            initialRoute = initialRoute
        ) {
            scene(route = "/home") {
                HomeScreen(goTo = navigator::navigate)
            }
            scene(route = "/settings") {
                SettingsScreen(goTo = navigator::navigate)
            }
            scene(route = "/voice-command") {
                VoiceCommandScreen(goTo = navigator::navigate)
            }
            scene(route = "/car-coords") {
                CarCoordsScreen(goTo = navigator::navigate)
            }
        }
    }
}
