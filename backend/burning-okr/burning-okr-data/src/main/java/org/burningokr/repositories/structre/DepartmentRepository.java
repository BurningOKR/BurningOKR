package org.burningokr.repositories.structre;

import org.burningokr.model.structures.Department;
import org.burningokr.repositories.ExtendedRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends ExtendedRepository<Department, Long> {}
