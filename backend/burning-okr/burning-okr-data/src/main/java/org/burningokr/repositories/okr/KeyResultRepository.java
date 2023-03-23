package org.burningokr.repositories.okr;

import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.Objective;
import org.burningokr.repositories.ExtendedRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface KeyResultRepository extends ExtendedRepository<KeyResult, Long> {
  @Query("SELECT k FROM KeyResult k WHERE k.parentObjective = ?1 ORDER BY sequence")
  List<KeyResult> findByObjectiveAndOrderBySequence(Objective objective);

  @Query("SELECT k FROM Objective o JOIN KeyResult WHERE o.parentOkrUnit = ?1")
  Collection<KeyResult> findKeyResultsOfUnit(long departmentId);
}
