package domain.repository

import domain.entity.Chat
import domain.entity.Command

interface CommandRepository {
    suspend fun findAll(): List<Command>
    suspend fun create(messages: List<Command>, chat: Chat?): Command
}