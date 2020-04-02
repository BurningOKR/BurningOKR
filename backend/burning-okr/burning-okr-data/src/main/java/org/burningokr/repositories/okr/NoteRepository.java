package org.burningokr.repositories.okr;

import org.burningokr.model.okr.Note;
import org.burningokr.repositories.ExtendedRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends ExtendedRepository<Note, Long> {}
