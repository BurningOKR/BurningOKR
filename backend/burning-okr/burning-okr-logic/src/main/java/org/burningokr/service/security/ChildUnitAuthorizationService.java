package org.burningokr.service.security;

import lombok.RequiredArgsConstructor;
import org.burningokr.model.okrUnits.OkrChildUnit;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.model.users.User;
import org.burningokr.service.okrUnit.OkrChildUnitService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChildUnitAuthorizationService {
  private final OkrChildUnitService<OkrChildUnit> childUnitService;
  private final AuthorizationUserContextService userContextService;

  public boolean hasManagerPrivilegesForChildUnit(Long childUnitId) {
    var childUnit = childUnitService.findById(childUnitId);
    boolean isOkrMaster = false;
    boolean isTopicSponsor = false;

    if (childUnit instanceof OkrDepartment department) {
      isOkrMaster = department.getOkrMasterId() == userContextService.getAuthenticatedUser()
              .getId();
      isTopicSponsor = department.getOkrTopicSponsorId() == userContextService.getAuthenticatedUser()
              .getId();
    }

    return isOkrMaster || isTopicSponsor || userContextService.getAuthenticatedUser().isAdmin();
  }

  public boolean hasMemberPrivilegesForChildUnit(Long childUnitId) {
    OkrChildUnit childUnit = childUnitService.findById(childUnitId);
    User authenticatedUser = userContextService.getAuthenticatedUser();
    boolean isMember = false;

    if (childUnit instanceof OkrDepartment department) {
      UUID authenticatedUserId = authenticatedUser.getId();
      isMember = department.getOkrMemberIds().contains(authenticatedUserId) || department.getOkrMasterId().equals(authenticatedUserId);
    }

    return isMember || authenticatedUser.isAdmin();
  }

}
