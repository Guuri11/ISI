package org.isi.domain.models

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.isi.data.LocalDateTimeSerializer

data class Fav(
    val id: String,
    val name: String,
    @Serializable(with = LocalDateTimeSerializer::class) @SerialName("createdAt") val createdAt: LocalDateTime,
    @Serializable(with = LocalDateTimeSerializer::class) @SerialName("updateAt") val updatedAt: LocalDateTime
)
