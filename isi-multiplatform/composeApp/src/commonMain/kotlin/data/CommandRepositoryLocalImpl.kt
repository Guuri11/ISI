package data

import domain.entity.*
import domain.mapper.createChatMessageFromCommand
import domain.mapper.createCommandFromString
import domain.repository.CommandRepository
import utils.OpenAIClient

class CommandRepositoryLocalImpl(model: GptSetting = GptSetting.GPT_4O_MINI) : CommandRepository {
    private var currentModel: GptSetting = GptSetting.GPT_4O_MINI

    init {
        currentModel = model
    }

    private fun createOpenAIClient(): OpenAIClient {
        return OpenAIClient()
    }

    override suspend fun findAll(): List<Command> {
        return emptyList()
    }

    override suspend fun create(messages: List<Command>, chat: Chat?, task: TaskType?): Command {
        val client = createOpenAIClient()

        val chatMessages = messages.map { createChatMessageFromCommand(it) }.toList()
        val response = client.getResponse(chatMessages, currentModel)

        return createCommandFromString(
            content = response,
            messageType = MessageType.ASSISTANT,
            task = TaskType.OTHER_TOPICS
        )
    }
}