package org.burningokr.repositories.okr;

import org.burningokr.model.okr.DefaultTaskBoardState;
import org.burningokr.repositories.ExtendedRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DefaultTaskStateRepository extends ExtendedRepository<DefaultTaskBoardState, Long> {

}
