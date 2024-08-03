package com.guuri11.isi_wear.domain

import kotlinx.serialization.Serializable

@Serializable
data class Chat(
    val id: String,
    val createdAt: String,
    val updatedAt: String
)
