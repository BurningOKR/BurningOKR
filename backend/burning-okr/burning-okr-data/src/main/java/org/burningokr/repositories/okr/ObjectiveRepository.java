package org.burningokr.repositories.okr;

import java.util.List;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.structures.SubStructure;
import org.burningokr.repositories.ExtendedRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ObjectiveRepository extends ExtendedRepository<Objective, Long> {
  @Query("SELECT o FROM Objective o WHERE o.parentStructure = ?1 ORDER BY sequence")
  List<Objective> findByStructureAndOrderBySequence(SubStructure department);
}
