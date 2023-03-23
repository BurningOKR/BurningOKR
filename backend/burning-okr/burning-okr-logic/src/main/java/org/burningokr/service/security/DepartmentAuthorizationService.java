package org.burningokr.service.security;

import lombok.RequiredArgsConstructor;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.service.okrUnit.OkrChildUnitService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DepartmentAuthorizationService {
  private final OkrChildUnitService<OkrDepartment> departmentService;

  public boolean hasAdminPrivilegesForDepartment(Long departmentId) {
    return true;
  }

  public boolean hasMemberPrivilegesForDepartment(Long departmentId) {
    return true;
  }

}
