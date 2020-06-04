package org.burningokr.controller.structure;

import java.util.Collection;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.burningokr.annotation.RestApiController;
import org.burningokr.dto.okr.ObjectiveDto;
import org.burningokr.dto.structure.StructureSchemaDto;
import org.burningokr.dto.structure.SubStructureDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.mapper.okr.ObjectiveMapper;
import org.burningokr.mapper.structure.StructureMapperPicker;
import org.burningokr.mapper.structure.StructureSchemaMapper;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.structures.Company;
import org.burningokr.model.structures.Department;
import org.burningokr.model.structures.SubStructure;
import org.burningokr.model.users.User;
import org.burningokr.service.structure.StructureService;
import org.burningokr.service.structure.StructureServicePicker;
import org.burningokr.service.structureutil.EntityCrawlerService;
import org.burningokr.service.userhandling.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestApiController
@RequiredArgsConstructor
public class StructureController {

  private final StructureServicePicker<SubStructure> structureServicePicker;
  private final StructureMapperPicker mapperPicker;
  private final EntityCrawlerService entityCrawlerService;
  private final UserService userService;
  private final StructureSchemaMapper structureSchemaMapper;
  private final ObjectiveMapper objectiveMapper;

  @GetMapping("/structures/{structureId}")
  public ResponseEntity<SubStructureDto> getStructureByStructureId(@PathVariable long structureId) {
    StructureService<SubStructure> structureService =
        structureServicePicker.getRoleServiceForDepartment(structureId);
    SubStructure structure = structureService.findById(structureId);
    DataMapper mapper = mapperPicker.getMapper(structure.getClass());
    return ResponseEntity.ok((SubStructureDto) mapper.mapEntityToDto(structure));
  }

  /**
   * API Endpoint to get the structure of all the related Departments.
   *
   * @param structureId a long value
   * @return a {@link ResponseEntity} ok with a {@link Collection} of DepartmentStructure
   */
  @GetMapping("/structures/{structureId}/structure")
  public ResponseEntity<Collection<StructureSchemaDto>> getDepartmentStructureOfDepartment(
      @PathVariable long structureId) {
    StructureService<SubStructure> structureService =
        structureServicePicker.getRoleServiceForDepartment(structureId);
    SubStructure structure = structureService.findById(structureId);
    Company parentCompany = entityCrawlerService.getCompanyOfStructure(structure);
    UUID currentUserId = userService.getCurrentUser().getId();
    return ResponseEntity.ok(
        structureSchemaMapper.mapStructureListToStructureSchemaList(
            parentCompany.getSubStructures(), currentUserId));
  }

  /**
   * API Endpoint to get all Objectives of a Department.
   *
   * @param departmentId a long value
   * @return a {@link ResponseEntity} ok with a {@link Collection} of Objectives
   */
  @GetMapping("/structures/{structureId}/objectives")
  public ResponseEntity<Collection<ObjectiveDto>> getObjectivesOfDepartment(
      @PathVariable long structureId) {
    StructureService<SubStructure> structureService =
        structureServicePicker.getRoleServiceForDepartment(structureId);
    Collection<Objective> objectives = structureService.findObjectivesOfStructure(structureId);
    return ResponseEntity.ok(objectiveMapper.mapEntitiesToDtos(objectives));
  }


  @PutMapping(value = "/structures/{structureId}", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<SubStructureDto> updateStructure(
      @PathVariable long structureId, @RequestBody SubStructureDto subStructureDto, User user) {
    StructureService<SubStructure> structureService =
        structureServicePicker.getRoleServiceForDepartment(structureId);

    SubStructure structure = structureService.findById(structureId);
    DataMapper mapper = mapperPicker.getMapper(structure.getClass());
    SubStructure receivedStructure = (SubStructure) mapper.mapDtoToEntity(subStructureDto);

    receivedStructure.setId(structureId);

    SubStructure updatedStructure = structureService.updateStructure(receivedStructure, user);

    return ResponseEntity.ok((SubStructureDto) mapper.mapEntityToDto(updatedStructure));
  }

  @DeleteMapping("/structures/{structureId}")
  public ResponseEntity deleteStructure(@PathVariable long structureId, User user) {
    StructureService<SubStructure> structureService =
        structureServicePicker.getRoleServiceForDepartment(structureId);
    structureService.deleteStructure(structureId, user);
    return ResponseEntity.ok().build();
  }
}
