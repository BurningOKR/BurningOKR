package org.burningokr.repositories.okr;

import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.histories.KeyResultHistory;
import org.burningokr.repositories.ExtendedRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface KeyResultHistoryRepository extends ExtendedRepository<KeyResultHistory, Long> {
  @Query("SELECT krh FROM KeyResultHistory krh WHERE krh.keyResult = ?1 ORDER BY dateChanged DESC ")
  List<KeyResultHistory> findByKeyResultOrderByDateChangedDesc(KeyResult keyResult);

  @Query("SELECT krh FROM KeyResultHistory krh WHERE krh.keyResult in (?1) ORDER BY dateChanged ASC ")
  KeyResultHistory findFirstByKeyResultInOrderByDateChangedDateChangedAsc(Collection<KeyResult> keyResults);

  @Query("SELECT krh FROM KeyResultHistory krh WHERE krh.keyResult in (?1) ORDER BY dateChanged DESC ")
  KeyResultHistory findFirstByKeyResultInOrderByDateChangedDateChangedDesc(Collection<KeyResult> keyResults);
}
