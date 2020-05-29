package org.burningokr.controller.structure;

import org.burningokr.annotation.RestApiController;
import org.burningokr.dto.structure.DepartmentDto;
import org.burningokr.dto.structure.SubStructureDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.mapper.structure.StructureMapperPicker;
import org.burningokr.model.structures.SubStructure;
import org.burningokr.model.users.User;
import org.burningokr.service.structure.StructureService;
import org.burningokr.service.structure.StructureServicePicker;
import org.burningokr.service.structure.departmentservices.StructureServiceUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestApiController
public class StructureController {

  private final StructureServicePicker<SubStructure> structureServicePicker;
  private final StructureMapperPicker mapperPicker;

  @Autowired
  public StructureController(
      StructureServicePicker<SubStructure> structureServicePicker,
      StructureMapperPicker mapperPicker
  ) {
    this.structureServicePicker = structureServicePicker;
    this.mapperPicker = mapperPicker;
  }

  @GetMapping("/structures/{structureId}")
  public ResponseEntity<SubStructureDto> getStructureByStructureId(@PathVariable long structureId) {
    StructureService<SubStructure> structureService = structureServicePicker.getRoleServiceForDepartment(structureId);
    SubStructure structure = structureService.findById(structureId);
    DataMapper mapper = mapperPicker.getMapper(structure.getClass());
    return ResponseEntity.ok((SubStructureDto) mapper.mapEntityToDto(structure));
  }

  @PutMapping("/structures/{structureId}")
  public ResponseEntity<SubStructureDto> updateStructure(@PathVariable long structureId, @Valid @RequestBody SubStructureDto subStructureDto, User user) {
    StructureService<SubStructure> structureService = structureServicePicker.getRoleServiceForDepartment(structureId);
    SubStructure structure = structureService.findById(structureId);
    DataMapper mapper = mapperPicker.getMapper(structure.getClass());
    SubStructure receivedStructure = (SubStructure) mapper.mapDtoToEntity(subStructureDto);

    receivedStructure.setId(structureId);

    structureService.updateStructure(receivedStructure, user);

    return ResponseEntity.ok((SubStructureDto) mapper.mapEntityToDto(receivedStructure));
  }

  @DeleteMapping("/structures/{departmentId}")
  public ResponseEntity deleteStructure(@PathVariable long structureId, User user) {
    StructureService<SubStructure> structureService = structureServicePicker.getRoleServiceForDepartment(structureId);
    structureService.deleteStructure(structureId, user);
    return ResponseEntity.ok().build();
  }
}
