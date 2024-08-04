package com.guuri11.isi.application.chat.service;

import com.guuri11.isi.domain.chat.Chat;
import com.guuri11.isi.infrastructure.persistance.ChatRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class ChatServiceCreateEntity {
    private final ChatRepository repository;

    public Chat createEntity() {
        final Chat entity = new Chat();

        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        repository.save(entity);
        return entity;
    }
}
