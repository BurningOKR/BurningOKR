package org.burningokr.service.excel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import org.burningokr.model.excel.ObjectiveRow;
import org.burningokr.model.excel.PercentageCellValue;
import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.okrUnits.OkrChildUnit;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.service.okrUnit.CompanyService;
import org.burningokr.service.okrUnit.departmentservices.BranchHelper;
import org.burningokr.service.okrUnit.departmentservices.OkrUnitServiceUsers;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ObjectiveRowBuilderService implements RowBuilderService<ObjectiveRow> {

  private final OkrUnitServiceUsers<OkrDepartment> departmentService;

  private final CompanyService companyService;

  public ObjectiveRowBuilderService(
      @Qualifier("okrUnitServiceUsers") OkrUnitServiceUsers<OkrDepartment> departmentService,
      CompanyService companyService) {
    this.departmentService = departmentService;
    this.companyService = companyService;
  }

  @Override
  public Collection<ObjectiveRow> generateForDepartment(long departmentId) {
    OkrDepartment okrDepartment = departmentService.findById(departmentId);
    return this.generateObjectiveRowCollectionForDepartment(okrDepartment);
  }

  @Override
  public Collection<ObjectiveRow> generateForCompany(long companyId) {
    OkrCompany okrCompany = companyService.findById(companyId);

    Collection<OkrChildUnit> okrChildUnitCollection = BranchHelper.collectChildUnits(okrCompany);

    return okrChildUnitCollection.stream()
        .flatMap(childUnit -> generateObjectiveRowCollectionForDepartment(childUnit).stream())
        .collect(Collectors.toList());
  }

  private Collection<ObjectiveRow> generateObjectiveRowCollectionForDepartment(
      OkrChildUnit okrChildUnit) {
    Collection<ObjectiveRow> rows = new ArrayList<>();

    okrChildUnit
        .getObjectives()
        .forEach(
            objective ->
                objective
                    .getKeyResults()
                    .forEach(
                        keyResult -> {
                          ObjectiveRow objectiveRow =
                              new ObjectiveRow(
                                  ifNullEmptyString(okrChildUnit.getName()),
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
