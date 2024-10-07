package domain.repository

import domain.entity.Chat
import domain.entity.Command
import domain.entity.TaskType

interface CommandRepository {
    suspend fun findAll(): List<Command>
    suspend fun create(messages: List<Command>, chat: Chat?, task: TaskType?): Command
}