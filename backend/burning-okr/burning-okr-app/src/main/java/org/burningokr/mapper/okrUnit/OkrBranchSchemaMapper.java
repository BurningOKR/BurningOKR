package org.burningokr.mapper.okrUnit;

import org.burningokr.dto.okrUnit.OkrDepartmentDtoRole;
import org.burningokr.dto.okrUnit.OkrUnitSchemaDto;
import org.burningokr.model.okrUnits.OkrChildUnit;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.model.okrUnits.OkrParentUnit;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@Service
public class OkrBranchSchemaMapper {

  /**
   * Maps OkrDepartment list to OkrDepartment okrUnit list.
   *
   * @param okrChildUnits a {@link Collection} of {@link OkrDepartment}
   * @param currentUserId an {@link UUID} object
   * @return a {@link Collection} of {@link OkrUnitSchemaDto}
   */
  public Collection<OkrUnitSchemaDto> mapOkrChildUnitListToOkrChildUnitSchemaList(
      Collection<? extends OkrChildUnit> okrChildUnits, UUID currentUserId) {
    ArrayList<OkrUnitSchemaDto> okrUnitSchemaList = new ArrayList<>();

    for (OkrChildUnit okrChildUnit : okrChildUnits) {
      okrUnitSchemaList.add(mapUnitToUnitchema(okrChildUnit, currentUserId));
    }

    return okrUnitSchemaList;
  }

  private OkrUnitSchemaDto mapUnitToUnitchema(OkrChildUnit okrChildUnit, UUID currentUserId) {
    OkrUnitSchemaDto unitSchemaDto = new OkrUnitSchemaDto();

    unitSchemaDto.setId(okrChildUnit.getId());
    unitSchemaDto.setName(okrChildUnit.getName());
    unitSchemaDto.setIsActive(okrChildUnit.isActive());
    unitSchemaDto.setUserRole(getRoleForUnit(okrChildUnit, currentUserId));
    unitSchemaDto.setIsTeam(okrChildUnit instanceof OkrDepartment);

    if (okrChildUnit instanceof OkrParentUnit) {
      unitSchemaDto.setSubDepartments(
          mapOkrChildUnitListToOkrChildUnitSchemaList(
              ((OkrParentUnit) okrChildUnit).getOkrChildUnits(), currentUserId));
    }

    return unitSchemaDto;
  }

  private OkrDepartmentDtoRole getRoleForUnit(OkrChildUnit okrChildUnit, UUID currentUserId) {

    if (okrChildUnit instanceof OkrDepartment) {
      OkrDepartment okrDepartment = (OkrDepartment) okrChildUnit;

      if (safeIsUuidSame(okrDepartment.getOkrMasterId(), currentUserId)
          || safeIsUuidSame(okrDepartment.getOkrTopicSponsorId(), currentUserId)) {
        return OkrDepartmentDtoRole.MANAGER;
      } else if (okrDepartment.getOkrMemberIds().contains(currentUserId)) {
        return OkrDepartmentDtoRole.MEMBER;
      } else {
        return OkrDepartmentDtoRole.USER;
      }
    } else {
      return OkrDepartmentDtoRole.USER;
    }
  }

  private boolean safeIsUuidSame(UUID unsafeUuid, UUID compareTo) {
    if (unsafeUuid != null) {
      return (unsafeUuid.equals(compareTo));
    }
    return false;
  }
}
