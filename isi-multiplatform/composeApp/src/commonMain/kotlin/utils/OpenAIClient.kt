package utils

import getHttpClient
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class OpenAIClient(private val apiKey: String) {

    private val client = getHttpClient()

    suspend fun getResponse(prompt: String): String {
        val response = client.post("https://api.openai.com/v1/chat/completions") {
            headers { bearerAuth(apiKey) }
            contentType(ContentType.Application.Json)
            setBody(OpenAIRequest(
                model = "gpt-3.5-turbo",
                messages = listOf(
                    ChatMessage("user", prompt)
                )
            ))
        }.body<OpenAIResponse>()

        return response.choices.first().message.content
    }
}

@kotlinx.serialization.Serializable
data class OpenAIRequest(
    val model: String,
    val messages: List<ChatMessage>
)

@kotlinx.serialization.Serializable
data class ChatMessage(
    val role: String,
    val content: String
)

@kotlinx.serialization.Serializable
data class OpenAIResponse(
    val choices: List<Choice>
)

@kotlinx.serialization.Serializable
data class Choice(
    val message: ChatMessage
)