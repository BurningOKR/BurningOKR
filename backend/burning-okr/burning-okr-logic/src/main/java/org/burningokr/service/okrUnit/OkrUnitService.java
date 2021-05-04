package org.burningokr.service.okrUnit;

import java.util.Collection;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.okr.okrTopicDraft.OkrTopicDraft;
import org.burningokr.model.okrUnits.OkrChildUnit;
import org.burningokr.model.okrUnits.OkrUnit;
import org.burningokr.model.users.User;

public interface OkrUnitService<T extends OkrUnit> {
  T findById(long UnitId);

  Collection<Objective> findObjectivesOfUnit(long departmentId);

  T updateUnit(T updatedUnit, User user);

  void deleteUnit(Long unitId, User user);

  OkrChildUnit createChildUnit(Long parentUnitId, OkrChildUnit okrChildUnit, User user);

  Objective createObjective(Long unitId, Objective objective, User user);

  OkrTopicDraft createTopicDraft(Long unitId, OkrTopicDraft topicDraft, User user);
}
