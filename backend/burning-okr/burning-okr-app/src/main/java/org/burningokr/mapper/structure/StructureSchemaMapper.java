package org.burningokr.mapper.structure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import org.burningokr.dto.structure.DepartmentDtoRole;
import org.burningokr.dto.structure.StructureSchemaDto;
import org.burningokr.model.structures.Department;
import org.burningokr.model.structures.ParentStructure;
import org.burningokr.model.structures.SubStructure;
import org.springframework.stereotype.Service;

@Service
public class StructureSchemaMapper {

  /**
   * Maps Department list to Department Structure list.
   *
   * @param subStructures a {@link Collection} of {@link Department}
   * @param currentUserId an {@link UUID} object
   * @return a {@link Collection} of {@link StructureSchemaDto}
   */
  public Collection<StructureSchemaDto> mapStructureListToStructureSchemaList(
      Collection<? extends SubStructure> subStructures, UUID currentUserId) {
    ArrayList<StructureSchemaDto> structureSchemaList = new ArrayList<>();

    for (SubStructure subStructure : subStructures) {
      structureSchemaList.add(mapStructureToStructureSchema(subStructure, currentUserId));
    }

    return structureSchemaList;
  }

  private StructureSchemaDto mapStructureToStructureSchema(
      SubStructure subStructure, UUID currentUserId) {
    StructureSchemaDto structureSchema = new StructureSchemaDto();

    structureSchema.setId(subStructure.getId());
    structureSchema.setName(subStructure.getName());
    structureSchema.setIsActive(subStructure.isActive());
    structureSchema.setUserRole(getRoleForStructure(subStructure, currentUserId));

    if (subStructure instanceof ParentStructure) {
      structureSchema.setSubDepartments(
          mapStructureListToStructureSchemaList(
              ((ParentStructure) subStructure).getSubStructures(), currentUserId));
    }

    return structureSchema;
  }

  private DepartmentDtoRole getRoleForStructure(SubStructure subStructure, UUID currentUserId) {

    if (subStructure instanceof Department) {
      Department department = (Department) subStructure;

      if (safeIsUuidSame(department.getOkrMasterId(), currentUserId)
          || safeIsUuidSame(department.getOkrTopicSponsorId(), currentUserId)) {
        return DepartmentDtoRole.MANAGER;
      } else if (department.getOkrMemberIds().contains(currentUserId)) {
        return DepartmentDtoRole.MEMBER;
      } else {
        return DepartmentDtoRole.USER;
      }
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
