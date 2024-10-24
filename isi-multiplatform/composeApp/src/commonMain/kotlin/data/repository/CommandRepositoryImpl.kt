package data.repository

import domain.Request
import domain.api.ApiPath
import domain.entity.Chat
import domain.entity.Command
import domain.entity.TaskType
import domain.repository.CommandRepository
import getHttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import utils.api

class CommandRepositoryImpl : CommandRepository {
    private val client = getHttpClient()

    override suspend fun findAll(): List<Command> {

        val commandResponse = client.get("$api/${ApiPath.COMMAND.value}").body<List<Command>>()

        if (commandResponse.isEmpty()) return emptyList()

        return commandResponse
    }

    override suspend fun create(messages: List<Command>, chat: Chat?, task: TaskType?, apiKey: String?): Command {
        val prompt = messages.last().content

        val commandResponse = client.post("$api/${ApiPath.COMMAND.value}") {
            contentType(ContentType.Application.Json)
            setBody(Request(prompt, chat, task))
        }.body<Command>()

        return commandResponse
    }
}