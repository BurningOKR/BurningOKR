package org.burningokr.repositories;

import java.io.Serializable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface ExtendedRepository<T, I extends Serializable> extends CrudRepository<T, I> {
  T findByIdOrThrow(I id);
}


