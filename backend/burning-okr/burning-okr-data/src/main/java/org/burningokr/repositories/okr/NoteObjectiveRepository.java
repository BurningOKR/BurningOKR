package org.burningokr.repositories.okr;

import org.burningokr.model.okr.NoteObjective;
import org.burningokr.repositories.ExtendedRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteObjectiveRepository extends ExtendedRepository<NoteObjective, Long> {}
