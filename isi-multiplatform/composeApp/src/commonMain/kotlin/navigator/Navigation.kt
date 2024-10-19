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
import ui.screens.Home
import ui.screens.Settings
import ui.screens.VoiceCommand
import utils.IntentSpeechToText
import utils.isLocal

@Composable
fun Navigation(navigator: Navigator, intentSpeechToText: IntentSpeechToText? = null) {
    val viewModel = viewModel(modelClass = IsiViewModel::class) {
        IsiViewModel(if (isLocal) CommandRepositoryLocalImpl() else CommandRepositoryImpl(), isLocal)
    }

    val initialRoute: String = if (intentSpeechToText != null && intentSpeechToText.equals(IntentSpeechToText.ACTION)) "/voice-command" else "/home"

    CompositionLocalProvider(LocalIsiViewModel provides viewModel) {
        NavHost(
            navigator = navigator,
            initialRoute = initialRoute
        ) {
            scene(route = "/home") {
                Home(goTo = navigator::navigate)
            }
            scene(route = "/settings") {
                Settings(goTo = navigator::navigate)
            }
            scene(route = "/voice-command") {
                VoiceCommand(goTo = navigator::navigate)
            }
        }
    }
}
