package com.guuri11.isi.domain.Fav;

import jakarta.persistence.*;
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
public class Fav {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  // TODO: handle exception in create service
  @Column(name = "name", unique = true)
  private String name;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

}
