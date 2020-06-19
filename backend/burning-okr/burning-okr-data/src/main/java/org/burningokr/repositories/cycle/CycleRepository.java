package org.burningokr.repositories.cycle;

import java.time.LocalDate;
import java.util.List;
import org.burningokr.model.cycles.Cycle;
import org.burningokr.model.cycles.CycleState;
import org.burningokr.model.cycles.OkrCompanyHistory;
import org.burningokr.repositories.ExtendedRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CycleRepository extends ExtendedRepository<Cycle, Long> {
  @Query(
      "SELECT c FROM Cycle c "
          + "JOIN c.companies a "
          + "WHERE a.history = ?1 "
          + "AND ?2 BETWEEN c.plannedStartDate AND c.plannedEndDate")
  List<Cycle> findByCompanyHistoryAndDateBetweenPlannedTimeRange(
      OkrCompanyHistory okrCompanyHistory, LocalDate dateToCheck);

  @Query(
      "SELECT c FROM Cycle c "
          + "JOIN c.companies a "
          + "WHERE a.history = ?1 "
          + "AND c.plannedStartDate > ?2 "
          + "AND c.plannedEndDate < ?3")
  List<Cycle> findByCompanyHistoryAndPlannedTimeRangeBetweenDates(
      OkrCompanyHistory okrCompanyHistory, LocalDate startDate, LocalDate endDate);

  @Query(
      "SELECT c from Cycle c "
          + "JOIN c.companies a "
          + "WHERE a.history = ?1 "
          + "AND c.plannedStartDate <= ?2 "
          + "AND c.cycleState <> ?3 "
          + "ORDER BY c.plannedEndDate DESC")
  List<Cycle>
      findByCompanyHistoryAndPlannedStartBeforeOrEqualAndNotCycleStateOrderByEndDateDescending(
          OkrCompanyHistory okrCompanyHistory, LocalDate currentDate, CycleState cycleStateNotToBe);
}
