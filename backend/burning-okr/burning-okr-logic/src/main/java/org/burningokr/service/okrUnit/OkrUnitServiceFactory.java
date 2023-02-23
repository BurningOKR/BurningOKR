package org.burningokr.service.okrUnit;

import lombok.RequiredArgsConstructor;
import org.burningokr.model.okrUnits.OkrChildUnit;
import org.burningokr.repositories.okrUnit.UnitRepository;
import org.burningokr.service.okrUnit.departmentservices.OkrUnitServiceAdmins;
import org.burningokr.service.okrUnit.departmentservices.OkrUnitServiceManagers;
import org.burningokr.service.okrUnit.departmentservices.OkrUnitServiceMembers;
import org.burningokr.service.okrUnit.departmentservices.OkrUnitServiceUsers;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OkrUnitServiceFactory<T extends OkrChildUnit> {

  private OkrUnitServiceUsers<T> userService;
  private OkrUnitServiceMembers<T> memberService;
  private OkrUnitServiceManagers<T> managerService;
  private OkrUnitServiceAdmins<T> adminService;
  private UnitRepository<T> unitRepository;

  /**
   * Gets the Role Service for a OkrDepartment for the role of the user.
   *
   * @param departmentId a long value
   * @return a {@link OkrUnitService} object
   */
  public OkrUnitService<T> getRoleServiceForDepartment(long departmentId) {
    OkrChildUnit childUnit = unitRepository.findByIdOrThrow(departmentId);
    // TODO fix auth
    return null;
  }

  public OkrUnitService<T> getUserOkrUnitService() {
    return this.userService;
  }
}
