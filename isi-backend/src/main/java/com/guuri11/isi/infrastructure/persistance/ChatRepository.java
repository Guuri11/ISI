package com.guuri11.isi.infrastructure.persistance;

import com.guuri11.isi.domain.Chat.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChatRepository extends JpaRepository<Chat, UUID> {
}
