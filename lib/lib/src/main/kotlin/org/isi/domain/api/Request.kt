package org.isi.domain.api

import kotlinx.serialization.Serializable
import org.isi.domain.models.Chat
import org.isi.domain.models.TaskType

@Serializable
data class Request(
    val request: String,
    val chat: Chat? = null,
    val task: TaskType? = null,
)