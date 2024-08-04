package com.guuri11.isi.application.chat;

 import java.time.LocalDateTime;
import java.util.UUID;

public record ChatDto(UUID id, LocalDateTime createdAt, LocalDateTime updateAt) {

}