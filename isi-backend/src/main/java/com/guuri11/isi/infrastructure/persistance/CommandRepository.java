package com.guuri11.isi.infrastructure.persistance;

import com.guuri11.isi.domain.Chat.Chat;
import com.guuri11.isi.domain.Command.Command;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CommandRepository extends JpaRepository<Command, UUID> {
    List<Command> findByChat(Chat chat);
}
