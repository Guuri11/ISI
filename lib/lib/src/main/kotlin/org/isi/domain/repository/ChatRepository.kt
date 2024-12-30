package org.isi.domain.repository

import org.isi.domain.models.Chat

interface ChatRepository {
    fun findAll(): List<Chat>
}