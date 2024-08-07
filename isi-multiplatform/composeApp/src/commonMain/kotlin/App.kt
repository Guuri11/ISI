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
import ui.theme.getColorsTheme

@Composable
@Preview
fun App() {
    Napier.base(DebugAntilog())

    PreComposeApp {
        val colors = getColorsTheme()
        /**
         * use of the http client
         *
         * var data by remember { mutableStateOf<String?>(null) }
         * val scope = rememberCoroutineScope()
         *
         * scope.launch {
         *   val response = callIsiServer(ApiPath.COMMAND)
         *   data = response.bodyAsText()
         * }
         */

        AppTheme {
            val navigator = rememberNavigator()
            Navigation(navigator)
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