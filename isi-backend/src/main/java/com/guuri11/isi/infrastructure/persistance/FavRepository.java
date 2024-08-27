package com.guuri11.isi.infrastructure.persistance;

import com.guuri11.isi.domain.fav.Fav;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FavRepository extends JpaRepository<Fav, UUID> {

  // Not the most elegant way in a scenario with multiple concurrent users. But as I am the only one using the application it works.
  Fav findFirstByOrderByCreatedAtDesc();
}