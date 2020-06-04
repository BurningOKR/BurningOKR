import { Injectable } from '@angular/core';
import { CorporateObjectiveStructureApiService } from '../api/corporate-objective-structure-api.service';
import { Observable } from 'rxjs';
import { CorporateObjectiveStructure } from '../../model/ui/OrganizationalUnit/corporate-objective-structure';
import { map } from 'rxjs/operators';
import { CorporateObjectiveStructureDto } from '../../model/api/structure/corporate-objective-structure.dto';
import { StructureType } from '../../model/api/structure/structure-type.enum';

@Injectable({
  providedIn: 'root'
})
export class CorporateObjectiveStructureMapper {

  constructor(private corporateObjectiveStructureApiService: CorporateObjectiveStructureApiService) {
  }

  static mapToCorporateObjectiveStructure(dto: CorporateObjectiveStructureDto): CorporateObjectiveStructure {
    return new CorporateObjectiveStructure(
      dto.structureId,
      dto.structureName,
      dto.objectiveIds,
      dto.label,
      dto.parentStructureId,
      dto.subStructureIds,
      dto.isActive,
      dto.isParentStructureACorporateObjectiveStructure
    );
  }

  static mapToCorporateObjectiveStructureDto(entity: CorporateObjectiveStructure): CorporateObjectiveStructureDto {
    const dto: CorporateObjectiveStructureDto = new CorporateObjectiveStructureDto();
    dto.structureId = entity.id;
    dto.isParentStructureACorporateObjectiveStructure = entity.isParentStructureACorporateObjectiveStructure;
    dto.structureName = entity.name;
    dto.objectiveIds = entity.objectives;
    dto.label = entity.label;
    dto.parentStructureId = entity.parentStructureId;
    dto.subStructureIds = entity.subStructureIds;
    dto.isActive = entity.isActive;
    dto.__structureType = StructureType.CORPORATE_OBJECTIVE_STRUCTURE;

    return dto;
  }

  getById$(id: number): Observable<CorporateObjectiveStructure> {
    return this.corporateObjectiveStructureApiService.getById$(id)
      .pipe(map(CorporateObjectiveStructureMapper.mapToCorporateObjectiveStructure));
  }

  create$(corporateObjectiveStructure: CorporateObjectiveStructure): Observable<CorporateObjectiveStructure> {
    return this.corporateObjectiveStructureApiService.create$(
      CorporateObjectiveStructureMapper.mapToCorporateObjectiveStructureDto(corporateObjectiveStructure))
      .pipe(map(CorporateObjectiveStructureMapper.mapToCorporateObjectiveStructure));
  }

  update$(id: number, corporateObjectiveStructure: CorporateObjectiveStructure): Observable<CorporateObjectiveStructure> {
    return this.corporateObjectiveStructureApiService.update$(
      id, CorporateObjectiveStructureMapper.mapToCorporateObjectiveStructureDto(corporateObjectiveStructure))
      .pipe(map(CorporateObjectiveStructureMapper.mapToCorporateObjectiveStructure));
  }

  delete$(id: number): Observable<boolean> {
    return this.corporateObjectiveStructureApiService.delete$(id);
  }
}
