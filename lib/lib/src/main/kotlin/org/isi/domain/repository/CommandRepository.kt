package org.isi.domain.repository

import org.isi.domain.models.Chat
import org.isi.domain.models.Command
import org.isi.domain.models.TaskType

interface CommandRepository {
    suspend fun findAll(): List<Command>
    suspend fun create(messages: List<Command>, chat: Chat?, task: TaskType?, apiKey: String?): Command
}