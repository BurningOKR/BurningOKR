package org.burningokr.repositories.okrUnit;

import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.repositories.ExtendedRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OkrDepartmentRepository extends ExtendedRepository<OkrDepartment, Long> {}
