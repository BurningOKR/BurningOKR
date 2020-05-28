package org.burningokr.service.structure;

import org.burningokr.model.structures.SubStructure;
import org.burningokr.repositories.structre.StructureRepository;
import org.burningokr.service.security.UserContextRole;
import org.burningokr.service.security.UserRoleFromContextService;
import org.burningokr.service.structure.departmentservices.StructureServiceAdmins;
import org.burningokr.service.structure.departmentservices.StructureServiceManagers;
import org.burningokr.service.structure.departmentservices.StructureServiceUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class StructureServicePicker<T extends SubStructure> {

  private StructureServiceUsers<T> userService;
  private StructureServiceManagers<T> managerService;
  private StructureServiceAdmins<T> adminService;
  private UserRoleFromContextService userRoleFromContextService;
  private StructureRepository<T> structureRepository;

  /**
   * Initialize DepartmentServicePicker.
   *
   * @param departmentServiceUsers a {@link StructureServiceUsers} object
   * @param departmentServiceManagers a {@link StructureServiceManagers} object
   * @param departmentServiceAdmins a {@link StructureServiceAdmins} object
   * @param userRoleFromContextService an {@link UserRoleFromContextService} object
   */
  @Autowired
  public StructureServicePicker(
      @Qualifier("structureServiceUsers") StructureServiceUsers<T> departmentServiceUsers,
      @Qualifier("structureServiceManagers") StructureServiceManagers<T> departmentServiceManagers,
      @Qualifier("structureServiceAdmins") StructureServiceAdmins<T> departmentServiceAdmins,
      UserRoleFromContextService userRoleFromContextService,
      StructureRepository<T> structureRepository) {
    this.userService = departmentServiceUsers;
    this.managerService = departmentServiceManagers;
    this.adminService = departmentServiceAdmins;
    this.userRoleFromContextService = userRoleFromContextService;
    this.structureRepository = structureRepository;
  }

  /**
   * Gets the Role Service for a Department for the role of the user.
   *
   * @param departmentId a long value
   * @return a {@link StructureService} object
   */
  public StructureService<T> getRoleServiceForDepartment(long departmentId) {
    SubStructure structure = structureRepository.findByIdOrThrow(departmentId);
    UserContextRole role = userRoleFromContextService.getUserRoleInStructure(structure);

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
