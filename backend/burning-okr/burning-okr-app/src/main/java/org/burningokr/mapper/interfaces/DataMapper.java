package org.burningokr.mapper.interfaces;

import java.util.Collection;

public interface DataMapper<M, D> {
  M mapDtoToEntity(D input);

  D mapEntityToDto(M input);
  // TODO implement using default methods
  Collection<M> mapDtosToEntities(Collection<D> input);

  Collection<D> mapEntitiesToDtos(Collection<M> input);
}
