
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.rememberNavigator
import moe.tlaster.precompose.viewmodel.viewModel
import navigator.Navigation
import org.jetbrains.compose.ui.tooling.preview.Preview
import platform.IntentSpeechToText
import presentation.IsiViewModel
import ui.theme.AppTheme

@Composable
@Preview
fun App(intentSpeechToText: IntentSpeechToText? = null, saveCarCoordinates: Boolean = false) {
    Napier.base(DebugAntilog())

    PreComposeApp {
        AppTheme(intentSpeechToText = intentSpeechToText) {
            val viewModel = viewModel(modelClass = IsiViewModel::class) {
                IsiViewModel()
            }

            LaunchedEffect(saveCarCoordinates) {
                if (saveCarCoordinates) {
                    viewModel.saveCarCoordinates()
                }
            }

            val navigator = rememberNavigator()
            Navigation(navigator, intentSpeechToText, viewModel)
        }
    }
}


expect fun getHttpClient(): HttpClient

fun createHttpClient(engine: HttpClientEngine): HttpClient {
    val json = Json { ignoreUnknownKeys = true }
    return HttpClient(engine) {
        install(ContentNegotiation) {
            json(json)
        }

        install(Logging) {
            level = LogLevel.BODY
        }
    }
}