package org.burningokr.mapper.okrUnit;

import java.util.HashMap;
import java.util.Map;
import org.burningokr.dto.okrUnit.OkrBranchDTO;
import org.burningokr.dto.okrUnit.OkrDepartmentDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.okrUnits.OkrBranch;
import org.burningokr.model.okrUnits.OkrChildUnit;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.springframework.stereotype.Service;

@Service
public class UnitMapperPicker {

  private final Map<Class<? extends OkrChildUnit>, DataMapper> mapperMap;

  public UnitMapperPicker(
      DataMapper<OkrDepartment, OkrDepartmentDto> departmentMapper,
      DataMapper<OkrBranch, OkrBranchDTO> OkrBranchMapper) {
    this.mapperMap = new HashMap<>();

    this.mapperMap.put(OkrDepartment.class, departmentMapper);
    this.mapperMap.put(OkrBranch.class, OkrBranchMapper);
  }

  public DataMapper getMapper(Class<? extends OkrChildUnit> clazz) {
    return mapperMap.get(clazz);
  }
}
