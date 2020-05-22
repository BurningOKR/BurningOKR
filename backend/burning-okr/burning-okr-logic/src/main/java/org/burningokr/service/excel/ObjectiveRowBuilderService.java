package org.burningokr.service.excel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import org.burningokr.model.excel.ObjectiveRow;
import org.burningokr.model.excel.PercentageCellValue;
import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.structures.Company;
import org.burningokr.model.structures.Department;
import org.burningokr.service.structure.CompanyService;
import org.burningokr.service.structure.departmentservices.DepartmentHelper;
import org.burningokr.service.structure.departmentservices.StructureServiceUsers;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ObjectiveRowBuilderService implements RowBuilderService<ObjectiveRow> {

  private final StructureServiceUsers departmentService;

  private final CompanyService companyService;

  public ObjectiveRowBuilderService(
      @Qualifier("departmentServiceUsers") StructureServiceUsers departmentService,
      CompanyService companyService) {
    this.departmentService = departmentService;
    this.companyService = companyService;
  }

  @Override
  public Collection<ObjectiveRow> generateForDepartment(long departmentId) {
    Department department = departmentService.findById(departmentId);
    return this.generateObjectiveRowCollectionForDepartment(department);
  }

  @Override
  public Collection<ObjectiveRow> generateForCompany(long companyId) {
    Company company = companyService.findById(companyId);

    Collection<Department> departments = DepartmentHelper.collectDepartments(company);

    return departments.stream()
        .flatMap(department -> generateObjectiveRowCollectionForDepartment(department).stream())
        .collect(Collectors.toList());
  }

  private Collection<ObjectiveRow> generateObjectiveRowCollectionForDepartment(
      Department department) {
    Collection<ObjectiveRow> rows = new ArrayList<>();

    department
        .getObjectives()
        .forEach(
            objective ->
                objective
                    .getKeyResults()
                    .forEach(
                        keyResult -> {
                          ObjectiveRow objectiveRow =
                              new ObjectiveRow(
                                  ifNullEmptyString(department.getName()),
                                  ifNullEmptyString(objective.getName()),
                                  new PercentageCellValue(getProgress(objective)),
                                  ifNullEmptyString(
                                      objective.getParentObjective() != null
                                          ? objective.getParentObjective().getName()
                                          : ""),
                                  ifNullEmptyString(keyResult.getName()),
                                  ifNullEmptyString(keyResult.getDescription()),
                                  keyResult.getStartValue(),
                                  keyResult.getTargetValue(),
                                  keyResult.getCurrentValue(),
                                  (keyResult.getUnit() != null
                                      ? keyResult.getUnit().toString()
                                      : ""));
                          rows.add(objectiveRow);
                        }));

    return rows;
  }

  private String ifNullEmptyString(String string) {
    return string != null ? string : "";
  }

  private float getProgress(Objective objective) {
    double progressValuesCombined =
        objective.getKeyResults().stream().mapToDouble(this::getKeyResultProgressNormalized).sum();
    int keyResultsCount = objective.getKeyResults().size();
    return (float) (progressValuesCombined / keyResultsCount);
  }

  private double getKeyResultProgressNormalized(KeyResult keyResult) {
    return Math.max(0, Math.min(1, getKeyResultProgress(keyResult)));
  }

  private double getKeyResultProgress(KeyResult keyResult) {
    double target = keyResult.getTargetValue() - keyResult.getStartValue();
    return (keyResult.getCurrentValue() - keyResult.getStartValue()) / target;
  }
}
