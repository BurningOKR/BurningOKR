package org.burningokr.mapper.interfaces;

import java.util.Collection;

public interface DataMapper<Entity, Dto> {
  Entity mapDtoToEntity(Dto dto);

  Dto mapEntityToDto(Entity entity);

  default Collection<Entity> mapDtosToEntities(Collection<Dto> dtos) {
    return dtos.stream().map(this::mapDtoToEntity).toList();
  }

  default Collection<Dto> mapEntitiesToDtos(Collection<Entity> entities) {
    return entities.stream().map(this::mapEntityToDto).toList();
  }
}
