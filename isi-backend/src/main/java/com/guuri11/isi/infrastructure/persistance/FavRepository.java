package com.guuri11.isi.infrastructure.persistance;

import com.guuri11.isi.domain.fav.Fav;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FavRepository extends JpaRepository<Fav, UUID> {

}