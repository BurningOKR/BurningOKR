import { Injectable } from '@angular/core';
import { CorporateObjectiveStructureApiService } from '../api/corporate-objective-structure-api.service';
import { Observable } from 'rxjs';
import { CorporateObjectiveStructure } from '../../model/ui/OrganizationalUnit/corporate-objective-structure';
import { map } from 'rxjs/operators';
import { CorporateObjectiveStructureDto } from '../../model/api/corporate-objective-structure.dto';

@Injectable({
  providedIn: 'root'
})
export class CorporateObjectiveStructureMapper {

  constructor(private corporateObjectiveStructureApiService: CorporateObjectiveStructureApiService) {
  }

  private static mapToCorporateObjectiveStructure(dto: CorporateObjectiveStructureDto): CorporateObjectiveStructure {
    return new CorporateObjectiveStructure(
      dto.id,
      dto.name,
      dto.objectiveIds,
      dto.label,
      dto.parentStructureId,
      dto.departmentIds,
      dto.corporateObjectiveStructureIds
    );
  }

  private static mapToCorporateObjectiveStructureDto(entity: CorporateObjectiveStructure): CorporateObjectiveStructureDto {
    return {
      id: entity.id,
      name: entity.name,
      objectiveIds: entity.objectives,
      label: entity.label,
      parentStructureId: entity.parentStructureId,
      departmentIds: entity.departmentIds,
      corporateObjectiveStructureIds: entity.corporateObjectiveStructureIds
    };
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
