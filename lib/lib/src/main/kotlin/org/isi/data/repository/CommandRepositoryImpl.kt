package org.isi.data.repository

import domain.api.ApiPath
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.isi.domain.api.Request
import org.isi.domain.models.Chat
import org.isi.domain.models.Command
import org.isi.domain.models.TaskType
import org.isi.domain.repository.CommandRepository

class CommandRepositoryImpl(server: String, httpClient: HttpClient) : CommandRepository {
    private val api = "$server/api/v1"
    private val client = httpClient

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