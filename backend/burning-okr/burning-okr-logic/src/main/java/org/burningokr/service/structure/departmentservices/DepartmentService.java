package org.burningokr.service.structure.departmentservices;

import java.util.Collection;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.structures.Department;
import org.burningokr.model.users.User;

public interface DepartmentService {
  Department findById(long departmentId);

  Collection<Department> findSubdepartmentsOfDepartment(long departmentId);

  Collection<Objective> findObjectivesOfDepartment(long departmentId);

  Department updateDepartment(Department updatedDepartment, User user);

  void deleteDepartment(Long departmentId, User user);

  Department createSubdepartment(Long parentDepartmentId, Department subDepartment, User user);

  Objective createObjective(Long departmentId, Objective objective, User user);
}
