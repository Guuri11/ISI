package com.guuri11.isi.application.Fav;


import java.time.LocalDateTime;
import java.util.UUID;

public record FavDto(
        UUID id,
        String name,
        LocalDateTime createdAt,
        LocalDateTime updateAt
) {

}