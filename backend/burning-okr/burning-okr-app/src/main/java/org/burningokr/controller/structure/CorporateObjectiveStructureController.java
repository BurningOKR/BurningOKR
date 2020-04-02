package org.burningokr.controller.structure;

import javax.validation.Valid;
import org.burningokr.annotation.RestApiController;
import org.burningokr.dto.structure.CorporateObjectiveStructureDto;
import org.burningokr.mapper.structure.CorporateObjectiveStructureMapper;
import org.burningokr.model.structures.CorporateObjectiveStructure;
import org.burningokr.model.users.User;
import org.burningokr.service.security.AuthorizationService;
import org.burningokr.service.structure.CorporateObjectiveStructureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestApiController
public class CorporateObjectiveStructureController {

  private final CorporateObjectiveStructureService corporateObjectiveStructureService;
  private final CorporateObjectiveStructureMapper corporateObjectiveStructureMapper;
  private final AuthorizationService authorizationService;

  /**
   * Initialize CorporateObjectiveStructureController.
   *
   * @param corporateObjectiveStructureService a {@link CorporateObjectiveStructureService} object
   * @param corporateObjectiveStructureMapper a {@link CorporateObjectiveStructureMapper} object
   * @param authorizationService an {@link AuthorizationService} object
   */
  @Autowired
  public CorporateObjectiveStructureController(
      CorporateObjectiveStructureService corporateObjectiveStructureService,
      CorporateObjectiveStructureMapper corporateObjectiveStructureMapper,
      AuthorizationService authorizationService) {
    this.corporateObjectiveStructureService = corporateObjectiveStructureService;
    this.corporateObjectiveStructureMapper = corporateObjectiveStructureMapper;
    this.authorizationService = authorizationService;
  }

  /**
   * API Endpoint to get a Corporate Objective Structure by ID.
   *
   * @param corporateObjectiveStructureId a long value
   * @return a {@link ResponseEntity} ok with a Corporate Objective Structure
   */
  @GetMapping("/corporateObjectiveStructure/{corporateObjectiveStructureId}")
  public ResponseEntity<CorporateObjectiveStructureDto> getCorporateObjectiveStructureById(
      @PathVariable long corporateObjectiveStructureId) {
    CorporateObjectiveStructure corporateObjectiveStructure =
        corporateObjectiveStructureService.findById(corporateObjectiveStructureId);
    return ResponseEntity.ok(
        corporateObjectiveStructureMapper.mapEntityToDto(corporateObjectiveStructure));
  }

  /**
   * API Endpoint to add a Corporate Objective Structure.
   *
   * @param corporateObjectiveStructureDto a {@link CorporateObjectiveStructureDto} object
   * @param user an {@link User} object
   * @return a {@link ResponseEntity} ok with a Corporate Objective Structure
   */
  @PostMapping("/corporateObjectiveStructure")
  @PreAuthorize("@authorizationService.isAdmin()")
  public ResponseEntity<CorporateObjectiveStructureDto> createCorporateObjectiveStructure(
      @Valid @RequestBody CorporateObjectiveStructureDto corporateObjectiveStructureDto,
      User user) {
    CorporateObjectiveStructure corporateObjectiveStructure =
        corporateObjectiveStructureMapper.mapDtoToEntity(corporateObjectiveStructureDto);
    CorporateObjectiveStructure persistedEntity =
        corporateObjectiveStructureService.create(corporateObjectiveStructure, user);
    return ResponseEntity.ok(corporateObjectiveStructureMapper.mapEntityToDto(persistedEntity));
  }

  /**
   * API Endpoint to update a Corporate Objective Structure.
   *
   * @param corporateObjectiveStructureId a long value
   * @param corporateObjectiveStructureDto a {@link CorporateObjectiveStructureDto} object
   * @param user an {@link User} object
   * @return a {@link ResponseEntity} ok with a Corporate Objective Structure
   */
  @PutMapping("/corporateObjectiveStructure/{corporateObjectiveStructureId}")
  @PreAuthorize("@authorizationService.isAdmin()")
  public ResponseEntity<CorporateObjectiveStructureDto> updateCorporateObjectiveStructure(
      @PathVariable long corporateObjectiveStructureId,
      @Valid @RequestBody CorporateObjectiveStructureDto corporateObjectiveStructureDto,
      User user) {
    CorporateObjectiveStructure corporateObjectiveStructure =
        corporateObjectiveStructureMapper.mapDtoToEntity(corporateObjectiveStructureDto);
    CorporateObjectiveStructure updatedEntity =
        corporateObjectiveStructureService.update(
            corporateObjectiveStructureId, corporateObjectiveStructure, user);
    return ResponseEntity.ok(corporateObjectiveStructureMapper.mapEntityToDto(updatedEntity));
  }

  /**
   * API Endpoint to delete a Corporate Objective Structure.
   *
   * @param corporateObjectiveStructureId a long value
   * @param user an {@link User} object
   * @return a {@link ResponseEntity} ok
   */
  @DeleteMapping("/corporateObjectiveStructure/{corporateObjectiveStructureId}")
  @PreAuthorize("@authorizationService.isAdmin()")
  public ResponseEntity deleteCorporateObjectiveStructure(
      @PathVariable long corporateObjectiveStructureId, User user) {
    corporateObjectiveStructureService.delete(corporateObjectiveStructureId, user);
    return ResponseEntity.ok().build();
  }
}
