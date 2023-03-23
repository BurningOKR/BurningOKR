package org.burningokr.mapper.okrUnit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.burningokr.dto.okrUnit.OkrBranchDto;
import org.burningokr.dto.okrUnit.OkrChildUnitDto;
import org.burningokr.dto.okrUnit.OkrDepartmentDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.okrUnits.OkrBranch;
import org.burningokr.model.okrUnits.OkrChildUnit;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.hibernate.TypeMismatchException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OkrChildUnitMapper implements DataMapper<OkrChildUnit, OkrChildUnitDto> {
  private final OkrDepartmentMapper okrDepartmentMapper;
  private final OkrBranchMapper okrBranchMapper;

  @Override
  public OkrChildUnit mapDtoToEntity(OkrChildUnitDto okrChildUnitDto) {
    if (okrChildUnitDto instanceof OkrDepartmentDto okrDepartment) {
      return okrDepartmentMapper.mapDtoToEntity(okrDepartment);
    } else if (okrChildUnitDto instanceof OkrBranchDto okrBranch) {
      return okrBranchMapper.mapDtoToEntity(okrBranch);
    } else {
      log.warn("Cannot map Unit with id: %d, unknown ChildUnitType".formatted(okrChildUnitDto.getOkrUnitId()));
      throw new TypeMismatchException("Cannot update Unit with id: %d, unknown ChildUnitType".formatted(okrChildUnitDto.getOkrUnitId()));
    }
  }

  @Override
  public OkrChildUnitDto mapEntityToDto(OkrChildUnit childUnit) {
    if (childUnit instanceof OkrDepartment okrDepartment) {
      return okrDepartmentMapper.mapEntityToDto(okrDepartment);
    } else if (childUnit instanceof OkrBranch okrBranch) {
      return okrBranchMapper.mapEntityToDto(okrBranch);
    } else {
      log.warn("Cannot map Unit with id: %d, unknown ChildUnitType".formatted(childUnit.getId()));
      throw new TypeMismatchException("Cannot update Unit with id: %d, unknown ChildUnitType".formatted(childUnit.getId()));
    }
  }
}
