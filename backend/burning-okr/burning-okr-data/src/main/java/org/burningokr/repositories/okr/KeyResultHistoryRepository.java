package org.burningokr.repositories.okr;

import java.util.Collection;
import java.util.List;
import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.histories.KeyResultHistory;
import org.burningokr.repositories.ExtendedRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface KeyResultHistoryRepository extends ExtendedRepository<KeyResultHistory, Long> {
  @Query("SELECT krh FROM KeyResultHistory krh WHERE krh.keyResult = ?1 ORDER BY dateChanged ASC ")
  List<KeyResultHistory> findByKeyResultOrderByDateChangedAsc(KeyResult keyResult);

  KeyResultHistory findTopByKeyResultInOrderByDateChangedAsc(Collection<KeyResult> keyResults);
}
