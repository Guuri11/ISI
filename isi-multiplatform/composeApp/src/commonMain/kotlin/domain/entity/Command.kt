package domain.entity

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import utils.LocalDateTimeSerializer

@Serializable
data class Command (
    val id: String,
    val log: String? = null,
    val chat: Chat,
    val content: String,
    val messageType: MessageType,
    val task: TaskType? = null,
    @Serializable(with = LocalDateTimeSerializer::class) @SerialName("createdAt") val createdAt: LocalDateTime,
    @Serializable(with = LocalDateTimeSerializer::class) @SerialName("updateAt") val updatedAt: LocalDateTime
)

enum class MessageType {
    ASSISTANT,
    USER
}

enum class TaskType(val value: String) {
    OTHER_TOPICS("Other"),
    BOOKMARK_RECOMMENDATIONS("Bookmarks"),
    REFACTOR("Refactor"),
    OPEN_APP("Open App"),
    LINKEDIN_OFFER_REJECTION("Linkedin Offer Rejections"),
    WEATHER("Weather"),
}