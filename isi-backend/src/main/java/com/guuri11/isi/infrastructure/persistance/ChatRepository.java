package com.guuri11.isi.infrastructure.persistance;

import com.guuri11.isi.domain.chat.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChatRepository extends JpaRepository<Chat, UUID> {
}
