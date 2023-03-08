package org.burningokr.service.okrUnit;

import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.okrUnits.OkrChildUnit;
import org.burningokr.model.okrUnits.OkrUnit;
import org.burningokr.model.users.IUser;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface OkrUnitService<T extends OkrUnit> {
  T findById(long UnitId);

  Collection<Objective> findObjectivesOfUnit(long departmentId);

  @Query("SELECT k FROM Objective o JOIN KeyResult WHERE o.parentOkrUnit = ?1")
  Collection<KeyResult> findKeyResultsOfUnit(long departmentId);

  T updateUnit(T updatedUnit, IUser IUser);

  void deleteUnit(Long unitId, IUser IUser);

  OkrChildUnit createChildUnit(Long parentUnitId, OkrChildUnit okrChildUnit, IUser IUser);

  Objective createObjective(Long unitId, Objective objective, IUser IUser);
}
