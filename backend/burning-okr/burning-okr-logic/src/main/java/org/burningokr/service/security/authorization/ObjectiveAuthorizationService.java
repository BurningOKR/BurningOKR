package org.burningokr.service.security.authorization;

import lombok.RequiredArgsConstructor;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.model.okrUnits.OkrUnit;
import org.burningokr.service.okr.ObjectiveService;
import org.burningokr.service.security.authenticationUserContext.AuthenticationUserContextService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ObjectiveAuthorizationService {

  private final ObjectiveService objectiveService;
    private final ChildUnitAuthorizationService childUnitAuthorizationService;
    private final AuthenticationUserContextService authenticationUserContextService;

  public boolean hasManagerPrivilegesForObjective(Long objectiveId) {
    OkrUnit parentOkrUnit = objectiveService.findById(objectiveId).getParentOkrUnit();

    if (parentOkrUnit instanceof OkrDepartment) {
      return childUnitAuthorizationService.hasManagerPrivilegesForChildUnit(parentOkrUnit.getId());
    } else {
        return authenticationUserContextService.getAuthenticatedUser().isAdmin();
    }
  }

  public boolean hasMemberPrivilegesForObjective(Long objectiveId) {
    OkrUnit parentOkrUnit = objectiveService.findById(objectiveId).getParentOkrUnit();

    if (parentOkrUnit instanceof OkrDepartment) {
      return childUnitAuthorizationService.hasMemberPrivilegesForChildUnit(parentOkrUnit.getId());
    } else {
        return authenticationUserContextService.getAuthenticatedUser().isAdmin();
    }
  }
}
