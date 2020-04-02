package org.burningokr.repositories.okr;

import java.util.List;
import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.Objective;
import org.burningokr.repositories.ExtendedRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface KeyResultRepository extends ExtendedRepository<KeyResult, Long> {
  @Query("SELECT k FROM KeyResult k WHERE k.parentObjective = ?1 ORDER BY sequence")
  List<KeyResult> findByObjectiveAndOrderBySequence(Objective objective);
}
