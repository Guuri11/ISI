package data

import domain.api.ApiPath
import domain.entity.Chat
import domain.entity.Command
import domain.repository.CommandRepository
import getHttpClient
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import utils.server

@Serializable
data class Request(
    val request: String,
    val chat: Chat? = null
)

class CommandRepositoryImpl : CommandRepository {
    private val client = getHttpClient()

    override suspend fun findAll(): List<Command> {

        val commandResponse = client.get("$server/${ApiPath.COMMAND.value}").body<List<Command>>()

        if (commandResponse.isEmpty()) return emptyList()

        return commandResponse
    }

    override suspend fun create(prompt: String, chat: Chat?): Command {
        val commandResponse = client.post("$server/${ApiPath.COMMAND.value}") {
            contentType(ContentType.Application.Json)
            setBody(Request(prompt, chat))
        }.body<Command>()

        return commandResponse;
    }
}