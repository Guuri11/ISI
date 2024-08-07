import io.ktor.client.*
import io.ktor.client.engine.android.*

actual fun getHttpClient(): HttpClient {
    return createHttpClient(Android.create())
}