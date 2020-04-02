package org.burningokr.model.structures;

import java.util.Collection;

public interface ParentStructure {
  Collection<Department> getDepartments();

  void setDepartments(Collection<Department> departments);
}
