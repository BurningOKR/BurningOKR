package org.burningokr.service.security.authorization;

import lombok.RequiredArgsConstructor;
import org.burningokr.model.okrUnits.OkrChildUnit;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.model.users.User;
import org.burningokr.service.okrUnit.OkrChildUnitService;
import org.burningokr.service.security.authenticationUserContext.AuthenticationUserContextService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChildUnitAuthorizationService {
    private final OkrChildUnitService<OkrChildUnit> childUnitService;
    private final AuthenticationUserContextService authenticationUserContextService;

  public boolean hasManagerPrivilegesForChildUnit(Long childUnitId) {
    var childUnit = childUnitService.findById(childUnitId);
    boolean isOkrMaster = false;
    boolean isTopicSponsor = false;

    if (childUnit instanceof OkrDepartment department) {
        isOkrMaster = department.getOkrMasterId() == authenticationUserContextService.getAuthenticatedUser()
                .getId();
        isTopicSponsor = department.getOkrTopicSponsorId() == authenticationUserContextService.getAuthenticatedUser()
                .getId();
    }

      return isOkrMaster || isTopicSponsor || authenticationUserContextService.getAuthenticatedUser().isAdmin();
  }

  public boolean hasMemberPrivilegesForChildUnit(Long childUnitId) {
      OkrChildUnit childUnit = childUnitService.findById(childUnitId);
      User authenticatedUser = authenticationUserContextService.getAuthenticatedUser();
      boolean isMember = false;

    if (childUnit instanceof OkrDepartment department) {
      UUID authenticatedUserId = authenticatedUser.getId();
      isMember = department.getOkrMemberIds().contains(authenticatedUserId) || department.getOkrMasterId().equals(authenticatedUserId);
    }

    return isMember || authenticatedUser.isAdmin();
  }

}
