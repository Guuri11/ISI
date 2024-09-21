package data

import domain.Request
import domain.api.ApiPath
import domain.entity.Chat
import domain.entity.Command
import domain.repository.CommandRepository
import getHttpClient
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import utils.api

class CommandRepositoryImpl : CommandRepository {
    private val client = getHttpClient()

    override suspend fun findAll(): List<Command> {

        val commandResponse = client.get("$api/${ApiPath.COMMAND.value}").body<List<Command>>()

        if (commandResponse.isEmpty()) return emptyList()

        return commandResponse
    }

    override suspend fun create(messages: List<Command>, chat: Chat?): Command {
        val prompt = messages.last().content

        val commandResponse = client.post("$api/${ApiPath.COMMAND.value}") {
            contentType(ContentType.Application.Json)
            setBody(Request(prompt, chat))
        }.body<Command>()

        return commandResponse
    }
}