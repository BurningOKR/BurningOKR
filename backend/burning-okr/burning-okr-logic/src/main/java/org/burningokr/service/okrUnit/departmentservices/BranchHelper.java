package org.burningokr.service.okrUnit.departmentservices;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;
import org.burningokr.model.okrUnits.OkrChildUnit;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.model.okrUnits.OkrParentUnit;

public class BranchHelper {

  /**
   * Gets all ChildUnits of a OkrCompany.
   *
   * @param okrCompany a {@link OkrCompany} object
   * @return a {@link Collection} of {@link OkrChildUnit}
   */
  public static Collection<OkrChildUnit> collectChildUnits(OkrCompany okrCompany) {
    Collection<OkrChildUnit> okrChildUnitCollection = new ArrayList<>();

    for (OkrChildUnit okrChildUnit : okrCompany.getOkrChildUnits()) {
      okrChildUnitCollection.addAll(collectChildUnits(okrChildUnit));
    }

    return okrChildUnitCollection;
  }

  /**
   * Gets ChildUnits directly under a OkrCompany
   *
   * @param okrCompany a {@link OkrCompany} object
   * @return a {@link Collection} of {@link OkrChildUnit}
   */

  public static Collection<OkrChildUnit> collectDirectChildUnits(OkrCompany okrCompany) {
    return okrCompany.getOkrChildUnits();
  }

  /**
   * Gets all Departments of a OkrCompany.
   *
   * @param okrCompany a {@link OkrCompany} object
   * @return a {@link Collection} of {@link OkrDepartment}
   */
  public static Collection<OkrDepartment> collectDepartments(OkrCompany okrCompany) {
    Collection<OkrChildUnit> okrChildUnitCollection = collectChildUnits(okrCompany);

    return okrChildUnitCollection.stream()
        .map(
            childUnit -> {
              if (childUnit instanceof OkrDepartment) {
                return (OkrDepartment) childUnit;
              } else {
                return null;
              }
            })
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

  public static Collection<OkrChildUnit> collectChildUnits(OkrChildUnit okrChildUnit) {
    Collection<OkrChildUnit> okrChildUnitCollection =
        new ArrayList<>(Collections.singletonList(okrChildUnit));

    if (okrChildUnit instanceof OkrParentUnit) {
      for (OkrChildUnit okrChildUnit1 : ((OkrParentUnit) okrChildUnit).getOkrChildUnits()) {
        okrChildUnitCollection.addAll(collectChildUnits(okrChildUnit1));
      }
    }

    return okrChildUnitCollection;
  }

  public static Collection<OkrChildUnit> collectChildUnitsWithoutSelf(OkrChildUnit okrChildUnit) {
    Collection<OkrChildUnit> okrChildUnitCollection = new ArrayList<>();

    if (okrChildUnit instanceof OkrParentUnit) {
      for (OkrChildUnit okrChildUnit1 : ((OkrParentUnit) okrChildUnit).getOkrChildUnits()) {
        okrChildUnitCollection.addAll(collectChildUnits(okrChildUnit1));
      }
    }

    return okrChildUnitCollection;
  }
}
