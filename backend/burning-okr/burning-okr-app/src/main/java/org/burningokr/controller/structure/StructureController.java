package org.burningokr.controller.structure;

import org.burningokr.annotation.RestApiController;
import org.burningokr.dto.structure.SubStructureDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.mapper.structure.StructureMapperPicker;
import org.burningokr.model.structures.SubStructure;
import org.burningokr.service.structure.departmentservices.StructureServiceUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestApiController
public class StructureController {

  private final StructureServiceUsers<SubStructure> structureService;
  private final StructureMapperPicker mapperPicker;

  @Autowired
  public StructureController(
      @Qualifier("structureServiceUsers") StructureServiceUsers<SubStructure> structureService,
      StructureMapperPicker mapperPicker
  ) {
    this.structureService = structureService;
    this.mapperPicker = mapperPicker;
  }

  @GetMapping("/structures/{structureId}")
  public ResponseEntity<SubStructureDto> getStructureByStructureId(@PathVariable long structureId) {
    SubStructure structure = structureService.findById(structureId);
    DataMapper mapper = mapperPicker.getMapper(structure.getClass());
    return ResponseEntity.ok((SubStructureDto) mapper.mapEntityToDto(structure));
  }
}
