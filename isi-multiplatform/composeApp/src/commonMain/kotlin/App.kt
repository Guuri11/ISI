import androidx.compose.runtime.Composable
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
import navigator.Navigation
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.theme.AppTheme
import platform.IntentSpeechToText

@Composable
@Preview
fun App(intentSpeechToText: IntentSpeechToText? = null) {
    Napier.base(DebugAntilog())

    PreComposeApp {
        AppTheme(intentSpeechToText = intentSpeechToText) {
            val navigator = rememberNavigator()
            Navigation(navigator, intentSpeechToText)
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