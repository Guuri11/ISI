package org.isi.domain.models

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.isi.data.LocalDateTimeSerializer

@Serializable
data class Command (
    val id: String,
    val log: String? = null,
    val chat: Chat,
    val content: String,
    val favName: String?,
    val messageType: MessageType,
    val task: TaskType? = null,
    @Serializable(with = LocalDateTimeSerializer::class) @SerialName("createdAt") val createdAt: LocalDateTime,
    @Serializable(with = LocalDateTimeSerializer::class) @SerialName("updatedAt") val updatedAt: LocalDateTime
)

