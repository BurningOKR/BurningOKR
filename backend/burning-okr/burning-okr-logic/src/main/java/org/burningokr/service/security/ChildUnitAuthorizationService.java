package org.burningokr.service.security;

import lombok.RequiredArgsConstructor;
import org.burningokr.model.okrUnits.OkrChildUnit;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.service.okrUnit.OkrChildUnitService;
import org.springframework.stereotype.Service;

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
    var childUnit = childUnitService.findById(childUnitId);
    var authenticatedUser = userContextService.getAuthenticatedUser();
    boolean isMember = false;

    if (childUnit instanceof OkrDepartment department) {
      isMember = department.getOkrMemberIds().contains(authenticatedUser.getId());
    }

    return isMember || authenticatedUser.isAdmin();
  }

}
