package com.guuri11.isi.domain.Chat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Chat {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
