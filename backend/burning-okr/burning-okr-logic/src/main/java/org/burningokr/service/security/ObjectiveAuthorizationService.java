package org.burningokr.service.security;

import lombok.RequiredArgsConstructor;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.model.okrUnits.OkrUnit;
import org.burningokr.service.okr.ObjectiveService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ObjectiveAuthorizationService {

  private final ObjectiveService objectiveService;
  private final ChildUnitAuthorizationService childUnitAuthorizationService;
  private final AuthorizationUserContextService authorizationUserContextService;

  public boolean hasManagerPrivilegesForObjective(Long objectiveId) {
    OkrUnit parentOkrUnit = objectiveService.findById(objectiveId).getParentOkrUnit();

    if (parentOkrUnit instanceof OkrDepartment) {
      return childUnitAuthorizationService.hasManagerPrivilegesForChildUnit(parentOkrUnit.getId());
    } else {
      return authorizationUserContextService.getAuthenticatedUser().isAdmin();
    }
  }

  public boolean hasMemberPrivilegesForObjective(Long objectiveId) {
    OkrUnit parentOkrUnit = objectiveService.findById(objectiveId).getParentOkrUnit();

    if (parentOkrUnit instanceof OkrDepartment) {
      return childUnitAuthorizationService.hasMemberPrivilegesForChildUnit(parentOkrUnit.getId());
    } else {
      return authorizationUserContextService.getAuthenticatedUser().isAdmin();
    }
  }
}
