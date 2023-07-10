package org.burningokr.service.security;

import lombok.RequiredArgsConstructor;
import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.model.okrUnits.OkrUnit;
import org.burningokr.service.okr.KeyResultService;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KeyResultAuthorizationService {

  private final ChildUnitAuthorizationService childUnitAuthorizationService;
  private final KeyResultService keyResultService;
  private final AuthorizationUserContextService authorizationUserContextService;

  public boolean hasMemberPrivilegesForKeyResult(Long keyResultId) {
    KeyResult keyResult = keyResultService.findById(keyResultId);
    Objective parentObjective = keyResult.getParentObjective();
    OkrUnit parentOkrUnitOfObjective = (OkrUnit) Hibernate.unproxy(parentObjective.getParentOkrUnit());

    if (parentOkrUnitOfObjective instanceof OkrDepartment) {
      return childUnitAuthorizationService.hasMemberPrivilegesForChildUnit(parentOkrUnitOfObjective.getId());
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
