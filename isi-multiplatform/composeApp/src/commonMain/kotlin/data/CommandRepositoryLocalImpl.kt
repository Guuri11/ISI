package data

import domain.entity.Chat
import domain.entity.Command
import domain.entity.MessageType
import domain.entity.TaskType
import domain.mapper.createCommandFromString
import domain.repository.CommandRepository
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import utils.OpenAIClient
import java.util.UUID

class CommandRepositoryLocalImpl : CommandRepository {
    private fun createOpenAIClient(): OpenAIClient {
        return OpenAIClient()
    }

    override suspend fun findAll(): List<Command> {
        return emptyList()
    }

    override suspend fun create(prompt: String, chat: Chat?): Command {
        val client = createOpenAIClient()

        val response = client.getResponse(prompt)

        return createCommandFromString(content = response, messageType = MessageType.ASSISTANT, task =  TaskType.OTHER_TOPICS);
    }
}