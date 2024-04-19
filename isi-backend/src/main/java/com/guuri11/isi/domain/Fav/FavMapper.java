package com.guuri11.isi.domain.Fav;

import com.guuri11.isi.application.Fav.FavDto;
import com.guuri11.isi.application.Fav.FavRequest;
import org.springframework.stereotype.Component;

@Component
public class FavMapper {

  public FavDto toDto(final Fav entity) {

    if (entity == null) {
      return null;
    }
    return new FavDto(entity.getId(), entity.getName(), entity.getCreatedAt(), entity.getUpdatedAt());
  }

  public Fav toEntity(final FavDto dto) {

    if (dto == null) {
      return null;
    }
    return new Fav(dto.id(), dto.name(), dto.createdAt(), dto.updateAt());
  }

  public Fav toEntity(final FavRequest request) {

    if (request == null) {
      return null;
    }
    final Fav fav = new Fav();
    fav.setName(request.name());
    return fav;
  }
}