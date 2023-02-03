package org.burningokr.mapper.okrUnit;

import org.burningokr.dto.okrUnit.OkrBranchDto;
import org.burningokr.dto.okrUnit.OkrDepartmentDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.okrUnits.OkrBranch;
import org.burningokr.model.okrUnits.OkrChildUnit;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UnitMapperFactory {

  private final Map<Class<? extends OkrChildUnit>, DataMapper> mapperMap;

  public UnitMapperFactory(
    DataMapper<OkrDepartment, OkrDepartmentDto> departmentMapper,
    DataMapper<OkrBranch, OkrBranchDto> OkrBranchMapper
  ) {
    this.mapperMap = new HashMap<>();

    this.mapperMap.put(OkrDepartment.class, departmentMapper);
    this.mapperMap.put(OkrBranch.class, OkrBranchMapper);
  }

  public DataMapper getMapper(Class<? extends OkrChildUnit> clazz) {
    return mapperMap.get(clazz);
  }
}
