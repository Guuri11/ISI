package utils

import domain.entity.GptSetting
import getHttpClient
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class OpenAIClient {

    private val client = getHttpClient()

    suspend fun getResponse(messages: List<ChatMessage>, model: GptSetting): String {
        val response = client.post("https://api.openai.com/v1/chat/completions") {
            headers { bearerAuth(openai_apikey) }
            contentType(ContentType.Application.Json)
            setBody(OpenAIRequest(
                model = model.value,
                messages = messages
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
