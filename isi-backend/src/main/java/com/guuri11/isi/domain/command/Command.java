package com.guuri11.isi.domain.command;

import com.guuri11.isi.domain.chat.Chat;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.ai.chat.messages.Media;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
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
    private Task task;
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