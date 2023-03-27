package org.burningokr.service.security;

import lombok.RequiredArgsConstructor;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.service.okrUnit.OkrChildUnitService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DepartmentAuthorizationService {
  private final OkrChildUnitService<OkrDepartment> departmentService;
  private final AuthorizationUserContextService userContextService;

  public boolean hasManagerPrivilegesForDepartment(Long departmentId) {
    var department = departmentService.findById(departmentId);
    var isOkrMaster = department.getOkrMasterId() == userContextService.getAuthenticatedUser()
      .getId();
    var isTopicSponsor = department.getOkrTopicSponsorId() == userContextService.getAuthenticatedUser()
      .getId();

    return isOkrMaster || isTopicSponsor;
  }

  public boolean hasMemberPrivilegesForDepartment(Long departmentId) {
    return departmentService.findById(departmentId).getOkrMemberIds().contains(
      userContextService.getAuthenticatedUser().getId()
    );
  }

}
