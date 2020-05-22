package org.burningokr.service.structure;

import java.util.Collection;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.structures.Structure;
import org.burningokr.model.users.User;

public interface StructureService<T extends Structure> {
  T findById(long structureId);

  Collection<T> findSubStructuresOfStructure(long structureId);

  Collection<Objective> findObjectivesOfStructure(long departmentId);

  T updateStructure(T updatedStructure, User user);

  void deleteStructure(Long structureId, User user);

  T createSubstructure(Long parentStructureId, T subStructure, User user);

  Objective createObjective(Long structureId, Objective objective, User user);
}
