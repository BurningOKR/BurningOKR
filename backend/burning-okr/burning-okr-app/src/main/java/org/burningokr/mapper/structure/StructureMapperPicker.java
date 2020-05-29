package org.burningokr.mapper.structure;

import org.burningokr.dto.structure.CorporateObjectiveStructureDto;
import org.burningokr.dto.structure.DepartmentDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.structures.CorporateObjectiveStructure;
import org.burningokr.model.structures.Department;
import org.burningokr.model.structures.SubStructure;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class StructureMapperPicker {

  private final Map<Class<? extends SubStructure>, DataMapper> mapperMap;

  public StructureMapperPicker(
      DataMapper<Department, DepartmentDto> departmentMapper,
      DataMapper<CorporateObjectiveStructure, CorporateObjectiveStructureDto> corporateObjectiveStructureMapper
  ) {
    this.mapperMap = new HashMap<>();

    this.mapperMap.put(Department.class, departmentMapper);
    this.mapperMap.put(CorporateObjectiveStructure.class, corporateObjectiveStructureMapper);
  }

  public DataMapper getMapper(Class<? extends SubStructure> clazz) {
    return mapperMap.get(clazz);
  }
}
