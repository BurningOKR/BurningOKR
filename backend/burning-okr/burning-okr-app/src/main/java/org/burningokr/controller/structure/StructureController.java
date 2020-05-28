package org.burningokr.controller.structure;

import lombok.RequiredArgsConstructor;
import org.burningokr.annotation.RestApiController;
import org.burningokr.dto.structure.DepartmentDto;
import org.burningokr.dto.structure.SubStructureDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@RestApiController
public class StructureController {

    @GetMapping("/structures/{structureId}")
    public ResponseEntity<SubStructureDto> getStructureByStructureId(
        @PathVariable long structureId) {
      return ResponseEntity.ok(new DepartmentDto());
    }
}
