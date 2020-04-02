package org.burningokr.mapper.interfaces;

import java.util.Collection;

public interface DataMapper<M, D> {
  M mapDtoToEntity(D input);

  D mapEntityToDto(M input);

  Collection<M> mapDtosToEntities(Collection<D> input);

  Collection<D> mapEntitiesToDtos(Collection<M> input);
}
