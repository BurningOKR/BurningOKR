package org.burningokr.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import java.io.Serializable;

public class ExtendedRepositoryImpl<T, I extends Serializable> extends SimpleJpaRepository<T, I>
  implements ExtendedRepository<T, I> {

  private EntityManager entityManager;

  public ExtendedRepositoryImpl(
    JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager
  ) {
    super(entityInformation, entityManager);
    this.entityManager = entityManager;
  }

  @Override
  public T findByIdOrThrow(I id) {
    return findById(id)
      .orElseThrow(
        () ->
          new EntityNotFoundException(
            "Entity mit id " + id + " konnte nicht gefunden werden"));
  }
}
