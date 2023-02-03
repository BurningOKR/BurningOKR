package org.burningokr.repositories.initialisation;

import org.burningokr.model.initialisation.InitState;
import org.burningokr.repositories.ExtendedRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InitStateRepository extends ExtendedRepository<InitState, Long> {
}
