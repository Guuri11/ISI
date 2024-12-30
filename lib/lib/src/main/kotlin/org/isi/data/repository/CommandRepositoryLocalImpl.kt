package org.isi.data.repository

import io.ktor.client.*
import org.isi.data.clients.OpenAIClient
import org.isi.domain.mapper.createChatMessageFromCommand
import org.isi.domain.mapper.createCommandFromString
import org.isi.domain.models.*
import org.isi.domain.repository.CommandRepository


class CommandRepositoryLocalImpl(model: GptSetting = GptSetting.GPT_4O_MINI, httpClient: HttpClient) : CommandRepository {
    private var currentModel: GptSetting = GptSetting.GPT_4O_MINI
    private var client = OpenAIClient(httpClient)

    init {
        currentModel = model
    }

    override suspend fun findAll(): List<Command> {
        return emptyList()
    }

    override suspend fun create(messages: List<Command>, chat: Chat?, task: TaskType?, apiKey: String?): Command {
        val chatMessages = messages.map { createChatMessageFromCommand(it) }.toList()
        val response = client.getResponse(chatMessages, currentModel, apiKey!!)

        return createCommandFromString(
            content = response,
            messageType = MessageType.ASSISTANT,
            task = TaskType.OTHER_TOPICS
        )
    }
}