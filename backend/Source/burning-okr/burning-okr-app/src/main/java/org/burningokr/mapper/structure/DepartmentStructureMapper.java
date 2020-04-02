package org.burningokr.mapper.structure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import org.burningokr.dto.structure.DepartmentDtoRole;
import org.burningokr.dto.structure.DepartmentStructureDto;
import org.burningokr.model.structures.Department;
import org.springframework.stereotype.Service;

@Service
public class DepartmentStructureMapper {

  /**
   * Maps Department list to Department Structure list.
   *
   * @param departments a {@link Collection} of {@link Department}
   * @param currentUserId an {@link UUID} object
   * @return a {@link Collection} of {@link DepartmentStructureDto}
   */
  public Collection<DepartmentStructureDto> mapDepartmentListToDepartmentStructureList(
      Collection<Department> departments, UUID currentUserId) {
    ArrayList<DepartmentStructureDto> microDepartmentList = new ArrayList<>();

    for (Department department : departments) {
      microDepartmentList.add(mapDepartmentToDepartmentStructure(department, currentUserId));
    }

    return microDepartmentList;
  }

  private DepartmentStructureDto mapDepartmentToDepartmentStructure(
      Department department, UUID currentUserId) {
    DepartmentStructureDto microDepartment = new DepartmentStructureDto();

    microDepartment.setId(department.getId());
    microDepartment.setName(department.getName());
    microDepartment.setIsActive(department.isActive());
    microDepartment.setUserRole(getRoleForDepartment(department, currentUserId));
    microDepartment.setSubDepartments(
        mapDepartmentListToDepartmentStructureList(department.getDepartments(), currentUserId));

    return microDepartment;
  }

  private DepartmentDtoRole getRoleForDepartment(Department department, UUID currentUserId) {
    if (safeIsUuidSame(department.getOkrMasterId(), currentUserId)
        || safeIsUuidSame(department.getOkrTopicSponsorId(), currentUserId)) {
      return DepartmentDtoRole.MANAGER;
    } else if (department.getOkrMemberIds().contains(currentUserId)) {
      return DepartmentDtoRole.MEMBER;
    } else {
      return DepartmentDtoRole.USER;
    }
  }

  private boolean safeIsUuidSame(UUID unsafeUuid, UUID compareTo) {
    if (unsafeUuid != null) {
      return (unsafeUuid.equals(compareTo));
    }
    return false;
  }
}
