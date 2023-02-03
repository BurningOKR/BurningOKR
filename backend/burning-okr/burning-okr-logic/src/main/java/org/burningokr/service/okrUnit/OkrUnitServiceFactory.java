package org.burningokr.service.okrUnit;

import org.burningokr.model.okrUnits.OkrChildUnit;
import org.burningokr.repositories.okrUnit.UnitRepository;
import org.burningokr.service.okrUnit.departmentservices.OkrUnitServiceAdmins;
import org.burningokr.service.okrUnit.departmentservices.OkrUnitServiceManagers;
import org.burningokr.service.okrUnit.departmentservices.OkrUnitServiceMembers;
import org.burningokr.service.okrUnit.departmentservices.OkrUnitServiceUsers;
import org.burningokr.service.security.UserContextRole;
import org.burningokr.service.security.UserRoleFromContextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class OkrUnitServiceFactory<T extends OkrChildUnit> {

  private OkrUnitServiceUsers<T> userService;
  private OkrUnitServiceMembers<T> memberService;
  private OkrUnitServiceManagers<T> managerService;
  private OkrUnitServiceAdmins<T> adminService;
  private UserRoleFromContextService userRoleFromContextService;
  private UnitRepository<T> unitRepository;

  /**
   * Initialize DepartmentServicePicker.
   *
   * @param departmentServiceUsers     a {@link OkrUnitServiceUsers} object
   * @param departmentServiceManagers  a {@link OkrUnitServiceManagers} object
   * @param departmentServiceAdmins    a {@link OkrUnitServiceAdmins} object
   * @param userRoleFromContextService an {@link UserRoleFromContextService} object
   */
  @Autowired
  public OkrUnitServiceFactory(
    @Qualifier("okrUnitServiceUsers") OkrUnitServiceUsers<T> departmentServiceUsers,
    @Qualifier("okrUnitServiceMembers") OkrUnitServiceMembers<T> departmentServiceMembers,
    @Qualifier("okrUnitServiceManagers") OkrUnitServiceManagers<T> departmentServiceManagers,
    @Qualifier("okrUnitServiceAdmins") OkrUnitServiceAdmins<T> departmentServiceAdmins,
    UserRoleFromContextService userRoleFromContextService,
    UnitRepository<T> unitRepository
  ) {
    this.userService = departmentServiceUsers;
    this.memberService = departmentServiceMembers;
    this.managerService = departmentServiceManagers;
    this.adminService = departmentServiceAdmins;
    this.userRoleFromContextService = userRoleFromContextService;
    this.unitRepository = unitRepository;
  }

  /**
   * Gets the Role Service for a OkrDepartment for the role of the user.
   *
   * @param departmentId a long value
   * @return a {@link OkrUnitService} object
   */
  public OkrUnitService<T> getRoleServiceForDepartment(long departmentId) {
    OkrChildUnit childUnit = unitRepository.findByIdOrThrow(departmentId);
    UserContextRole role = userRoleFromContextService.getUserRoleInUnit(childUnit);

    switch (role) {
      case OKRMANAGER:
        return managerService;
      case ADMIN:
        return adminService;
      case OKRMEMBER:
        return memberService;
      default:
        return userService;
    }
  }

  public OkrUnitService<T> getUserOkrUnitService() {
    return this.userService;
  }
}
