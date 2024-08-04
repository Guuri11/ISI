package com.guuri11.isi.domain.fav;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
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
