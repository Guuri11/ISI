package com.guuri11.isi_wear.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Command (
    val id: String,
    val log: String? = null,
    val chat: Chat,
    val content: String,
    val messageType: String,
    val task: String? = null,
    @SerialName("createdAt") val createdAt: String,
    @SerialName("updateAt") val updatedAt: String
)