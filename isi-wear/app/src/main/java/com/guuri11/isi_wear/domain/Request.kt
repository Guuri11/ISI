package com.guuri11.isi_wear.domain

import kotlinx.serialization.Serializable

@Serializable
data class Request(
    val request: String,
    val chat: Chat? = null
)