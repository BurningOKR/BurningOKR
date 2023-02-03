package org.burningokr.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface ExtendedRepository<T, I extends Serializable> extends CrudRepository<T, I> {
  T findByIdOrThrow(I id);
}
