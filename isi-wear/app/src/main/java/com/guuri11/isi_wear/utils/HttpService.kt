package com.guuri11.isi_wear.utils

import android.util.Log
import com.guuri11.isi_wear.BuildConfig
import com.guuri11.isi_wear.domain.ErrorMessage
import dev.ai4j.openai4j.OpenAiClient
import dev.ai4j.openai4j.chat.ChatCompletionModel
import dev.ai4j.openai4j.chat.ChatCompletionRequest
import dev.ai4j.openai4j.chat.Message
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import java.util.*

object HTTPService {

    private const val BASE_URL = BuildConfig.SERVER + "/api/v1/commands"
    private val json = Json { ignoreUnknownKeys = true }
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(json)
        }
        // TODO: review logging in ktor client
    }
    interface Callback {
        fun onSuccess(response: String)
        fun onError(error: String)
    }

    fun sendCommand(result: String, callback: Callback, chatId: UUID?) {
        Log.i("HTTP Service", "Sending command with result $result and chatId $chatId")
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val url = BASE_URL
                val response = postRequest(url, result, chatId)

                if (response.status.value == 200) {
                    withContext(Dispatchers.Main) {
                        callback.onSuccess(response.bodyAsText())
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        callback.onError("${ErrorMessage.CANT_PERFOM_REQUEST} ${response.status.value}")
                    }
                }
            } catch (e: Exception) {
                Log.e("HTTP Service", "Exception cached: ${e.message} |||| ${e}")
                withContext(Dispatchers.Main) {
                    callback.onError(e.message ?: "Unknown error")
                }
            }
        }
    }

    private suspend fun postRequest(url: String, result: String, chatId: UUID?): HttpResponse {
        Log.i("HTTP Service", "chatId: $chatId")
        val jsonInputString = if (chatId != null) {
            Json.encodeToString(mapOf("request" to result, "chat" to mapOf("id" to chatId.toString())))
        } else {
            Json.encodeToString(mapOf("request" to result))
        }

        return client.post(url) {
            setBody(jsonInputString)
            contentType(ContentType.Application.Json)
        }
    }

    interface GptLocalCallback {
        fun onResponse(response: String)
        fun onError(errorMessage: String)
    }

    fun gptLocal(messages: List<Message>, callback: GptLocalCallback) {
        val openAiKey = BuildConfig.OPEN_AI_API_KEY
        Log.d("HTTPService", "OpenAI API Key: $openAiKey")

        val client = OpenAiClient.builder()
            .openAiApiKey(openAiKey)
            .build()

        val request = ChatCompletionRequest.builder()
            .model(ChatCompletionModel.GPT_3_5_TURBO_0125)
            .messages(messages)
            .build()

        client.chatCompletion(request)
            .onResponse { response -> callback.onResponse(response.content()) }
            .onError { error -> callback.onError(error.message ?: "Unknown error") }
            .execute()
    }
}
