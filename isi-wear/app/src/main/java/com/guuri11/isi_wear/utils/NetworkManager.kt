package com.guuri11.isi_wear.utils

import android.content.Context
import android.util.Log
import com.guuri11.isi_wear.BuildConfig
import com.guuri11.isi_wear.domain.Command
import dev.ai4j.openai4j.chat.AssistantMessage
import dev.ai4j.openai4j.chat.Message
import dev.ai4j.openai4j.chat.UserMessage
import kotlinx.serialization.json.Json
import java.util.*

class NetworkManager(private val context: Context) {
    private var chatId: UUID? = null

    companion object {
        var localAssistant = false
        val messages = mutableListOf<Message>()
    }

    interface NetworkCallback {
        fun onCommandSuccess(response: String)
        fun onCommandError(error: String)
    }

    fun sendCommand(command: String, callback: NetworkCallback) {
        val wifiUtils = WifiService()
        val wifiSSID = BuildConfig.WIFI_SSID
        val isConnectedToExpectedWifi: Boolean = wifiUtils.isConnectedToWifi(context, "\"" + wifiSSID + "\"")

        if (isConnectedToExpectedWifi && !localAssistant) {
            sendToBackend(command, callback)
        } else {
            sendToLocalGPT(command, callback)
        }
    }

    private fun sendToBackend(command: String, callback: NetworkCallback) {
        HTTPService.sendCommand(command, object : HTTPService.Callback {
            private val json = Json { ignoreUnknownKeys = true }

            override fun onSuccess(response: String) {
                Log.i("NetworkManager", "response from backend: $response")

                try {
                    val commandDto = json.decodeFromString<Command>(response)
                    chatId = UUID.fromString(commandDto.chat.id)
                    callback.onCommandSuccess(commandDto.content)
                } catch (e: Exception) {
                    println(e)
                    callback.onCommandError(e.message ?: "Serialization error")
                }
            }

            override fun onError(error: String) {
                callback.onCommandError(error)
            }
        }, chatId)
    }

    private fun sendToLocalGPT(command: String, callback: NetworkCallback) {
        messages.add(UserMessage.from(command))
        HTTPService.gptLocal(messages, object : HTTPService.GptLocalCallback {
            override fun onResponse(response: String) {
                messages.add(AssistantMessage.from(response))
                callback.onCommandSuccess(response)
            }

            override fun onError(errorMessage: String) {
                callback.onCommandError(errorMessage)
            }
        })
    }
}
