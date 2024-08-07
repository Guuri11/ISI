package domain.repository

import domain.entity.Chat

interface ChatRepository {
    fun findAll(): List<Chat>
}