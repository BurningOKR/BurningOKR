package org.burningokr.service.structure;

import org.burningokr.model.structures.Department;
import org.burningokr.service.security.UserContextRole;
import org.burningokr.service.security.UserRoleFromContextService;
import org.burningokr.service.structure.departmentservices.DepartmentServiceAdmins;
import org.burningokr.service.structure.departmentservices.DepartmentServiceManagers;
import org.burningokr.service.structure.departmentservices.DepartmentServiceUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepartmentServicePicker {

  private DepartmentServiceUsers userService;
  private DepartmentServiceManagers managerService;
  private DepartmentServiceAdmins adminService;
  private UserRoleFromContextService userRoleFromContextService;

  /**
   * Initialize DepartmentServicePicker.
   *
   * @param departmentServiceUsers a {@link DepartmentServiceUsers} object
   * @param departmentServiceManagers a {@link DepartmentServiceManagers} object
   * @param departmentServiceAdmins a {@link DepartmentServiceAdmins} object
   * @param userRoleFromContextService an {@link UserRoleFromContextService} object
   */
  @Autowired
  public DepartmentServicePicker(
      DepartmentServiceUsers departmentServiceUsers,
      DepartmentServiceManagers departmentServiceManagers,
      DepartmentServiceAdmins departmentServiceAdmins,
      UserRoleFromContextService userRoleFromContextService) {
    this.userService = departmentServiceUsers;
    this.managerService = departmentServiceManagers;
    this.adminService = departmentServiceAdmins;
    this.userRoleFromContextService = userRoleFromContextService;
  }

  /**
   * Gets the Role Service for a Department for the role of the user.
   *
   * @param departmentId a long value
   * @return a {@link StructureService} object
   */
  public StructureService<Department> getRoleServiceForDepartment(long departmentId) {
    UserContextRole role = userRoleFromContextService.getUserRoleInDepartmentId(departmentId);

    switch (role) {
      case OKRMANAGER:
        return managerService;
      case ADMIN:
        return adminService;
      default:
        return userService;
    }
  }
}
