package org.burningokr.service.structure.departmentservices;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;
import org.burningokr.model.structures.Company;
import org.burningokr.model.structures.Department;
import org.burningokr.model.structures.ParentStructure;
import org.burningokr.model.structures.SubStructure;

public class StructureHelper {

  /**
   * Gets all SubStructures of a Company.
   *
   * @param company a {@link Company} object
   * @return a {@link Collection} of {@link SubStructure}
   */
  public static Collection<SubStructure> collectSubStructures(Company company) {
    Collection<SubStructure> subStructureCollection = new ArrayList<>();

    for (SubStructure subStructure : company.getSubStructures()) {
      subStructureCollection.addAll(collectSubStructures(subStructure));
    }

    return subStructureCollection;
  }

  /**
   * Gets all Departments of a Company.
   *
   * @param company a {@link Company} object
   * @return a {@link Collection} of {@link Department}
   */
  public static Collection<Department> collectDepartments(Company company) {
    Collection<SubStructure> subStructureCollection = collectSubStructures(company);

    return subStructureCollection.stream()
        .map(
            subStructure -> {
              if (subStructure instanceof Department) {
                return (Department) subStructure;
              } else {
                return null;
              }
            })
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

  private static Collection<SubStructure> collectSubStructures(SubStructure subStructure) {
    Collection<SubStructure> subStructureCollection =
        new ArrayList<>(Collections.singletonList(subStructure));

    if (subStructure instanceof ParentStructure) {
      for (SubStructure subStructure1 : ((ParentStructure) subStructure).getSubStructures()) {
        subStructureCollection.addAll(collectSubStructures(subStructure1));
      }
    }

    return subStructureCollection;
  }
}
