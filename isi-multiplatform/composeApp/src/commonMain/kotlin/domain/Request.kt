package domain

import domain.entity.Chat
import domain.entity.TaskType
import kotlinx.serialization.Serializable

@Serializable
data class Request(
    val request: String,
    val chat: Chat? = null,
    val task: TaskType? = null,
)