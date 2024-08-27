package domain

import domain.entity.Chat
import kotlinx.serialization.Serializable

@Serializable
data class Request(
    val request: String,
    val chat: Chat? = null
)