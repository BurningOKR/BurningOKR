import { Injectable } from '@angular/core';
import { SubStructure } from '../../model/ui/OrganizationalUnit/sub-structure';
import { Observable } from 'rxjs';
import { StructureId } from '../../model/id-types';
import { StructureApiService } from '../api/structure-api.service';
import { DepartmentUnit } from '../../model/ui/OrganizationalUnit/department-unit';
import { CorporateObjectiveStructure } from '../../model/ui/OrganizationalUnit/corporate-objective-structure';
import { DepartmentMapper } from './department.mapper';
import { CorporateObjectiveStructureMapper } from './corporate-objective-structure.mapper';
import { SubStructureDto } from '../../model/api/structure/sub-structure.dto';
import { DepartmentDto } from '../../model/api/structure/department.dto';
import { CorporateObjectiveStructureDto } from '../../model/api/structure/corporate-objective-structure.dto';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class StructureMapper {

  constructor(private structureService: StructureApiService) { }

  private static mapToDto(subStructure: SubStructure): SubStructureDto {
    if (subStructure instanceof DepartmentUnit) {
      return DepartmentMapper.mapDepartmentUnit(subStructure);
    } else if (subStructure instanceof CorporateObjectiveStructure) {
      return CorporateObjectiveStructureMapper.mapToCorporateObjectiveStructureDto(subStructure);
    }
  }

  private static mapToEntity(subStructure: SubStructureDto): SubStructure {
    if (subStructure instanceof DepartmentDto) {
      return DepartmentMapper.mapDepartmentDto(subStructure);
    } else if (subStructure instanceof CorporateObjectiveStructureDto) {
      return CorporateObjectiveStructureMapper.mapToCorporateObjectiveStructure(subStructure);
    }
  }

  getSubStructureById$(id: StructureId): Observable<SubStructure> {
    return this.structureService.getSubStructureById$(id)
      .pipe(
        map((structure: SubStructureDto) => StructureMapper.mapToEntity(structure))
      );
  }

  putSubStructure$(subStructure: SubStructure): Observable<SubStructure> {
    return this.structureService.putSubStructure$(subStructure.id, StructureMapper.mapToDto(subStructure))
      .pipe(
        map((structure: SubStructureDto) => StructureMapper.mapToEntity(structure))
      );
  }

  deleteSubStructure$(subStructure: SubStructure): Observable<boolean> {
    return this.structureService.deleteSubStructure$(subStructure.id);
  }
}
