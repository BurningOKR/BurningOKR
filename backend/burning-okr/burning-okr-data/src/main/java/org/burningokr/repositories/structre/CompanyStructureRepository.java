package org.burningokr.repositories.structre;

import org.burningokr.model.structures.Structure;
import org.burningokr.repositories.ExtendedRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyStructureRepository extends ExtendedRepository<Structure, Long> {}
