package org.burningokr.service.security;

import lombok.RequiredArgsConstructor;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.model.okrUnits.OkrUnit;
import org.burningokr.service.okr.KeyResultService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KeyResultAuthorizationService {

  private final ObjectiveAuthorizationService objectiveAuthorizationService;
  private final ChildUnitAuthorizationService childUnitAuthorizationService;
  private final KeyResultService keyResultService;
  private final AuthorizationUserContextService authorizationUserContextService;

  public boolean hasMemberPrivilegesForKeyResult(Long keyResultId) {
    OkrUnit parentOkrUnit = keyResultService.findById(keyResultId).getParentObjective().getParentOkrUnit();

    if (parentOkrUnit instanceof OkrDepartment) {
      return childUnitAuthorizationService.hasMemberPrivilegesForChildUnit(parentOkrUnit.getId());
    } else {
      return authorizationUserContextService.getAuthenticatedUser().isAdmin();
    }
  }

  public boolean hasManagerPrivilegesForKeyResult(Long keyResultId) {
    OkrUnit parentOkrUnit = keyResultService.findById(keyResultId).getParentObjective().getParentOkrUnit();

    if (parentOkrUnit instanceof OkrDepartment) {
      return childUnitAuthorizationService.hasManagerPrivilegesForChildUnit(parentOkrUnit.getId());
    } else {
      return authorizationUserContextService.getAuthenticatedUser().isAdmin();
    }
  }
}
