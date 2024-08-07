package domain.entity

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import utils.LocalDateTimeSerializer

data class Fav(
    val id: String,
    val name: String,
    @Serializable(with = LocalDateTimeSerializer::class) @SerialName("createdAt") val createdAt: LocalDateTime,
    @Serializable(with = LocalDateTimeSerializer::class) @SerialName("updateAt") val updatedAt: LocalDateTime
)
