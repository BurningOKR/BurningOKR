package org.burningokr.service.structure.departmentservices;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import org.burningokr.model.structures.Company;
import org.burningokr.model.structures.Department;

public class DepartmentHelper {

  /**
   * Gets all Departments and child Departments of a Company.
   *
   * @param company a {@link Company} object
   * @return a {@link Collection} of {@link Department}
   */
  public static Collection<Department> collectDepartments(Company company) {
    Collection<Department> departmentCollection = new ArrayList<>();

    for (Department department : company.getDepartments()) {
      departmentCollection.addAll(collectDepartments(department));
    }

    return departmentCollection;
  }

  private static Collection<Department> collectDepartments(Department department) {
    Collection<Department> departmentCollection =
        new ArrayList<>(Collections.singletonList(department));

    for (Department department1 : department.getDepartments()) {
      departmentCollection.addAll(collectDepartments(department1));
    }

    return departmentCollection;
  }
}
