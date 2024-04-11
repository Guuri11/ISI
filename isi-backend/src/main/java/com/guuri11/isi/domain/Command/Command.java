package com.guuri11.isi.domain.Command;

import com.guuri11.isi.domain.Chat.Chat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.chat.messages.Media;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Command implements Message {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String log;
    @Column(columnDefinition = "TEXT")
    private String content;
    private MessageType messageType;
    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Override
    public List<Media> getMedia() {
        return List.of();
    }

    @Override
    public Map<String, Object> getProperties() {
        return Map.of();
    }
}