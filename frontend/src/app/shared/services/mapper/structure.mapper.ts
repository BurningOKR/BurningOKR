import { Injectable } from '@angular/core';
import { StructureApiService } from '../api/structure-api.service';
import { Observable } from 'rxjs';
import { Structure } from '../../model/ui/OrganizationalUnit/structure';
import { StructureDto } from '../../model/api/OkrUnit/structure.dto';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class StructureMapper {

  constructor(
    private structureApiService: StructureApiService,
  ) {
  }

  getSchemaOfAllExistingStructures$(): Observable<Structure[]> {
    return this.structureApiService
      .getSchemaOfAllExistingStructures$().pipe(
        map(companies => this.mapDtosToStructureArray(companies)),
      );
  }

  getSchemaOfAllActiveStructuresWithCycleName$(): Observable<Structure[]> {
    return this.structureApiService
      .getSchemaOfAllActiveStructuresWithCycleName$().pipe(
        map(companies => this.mapDtosToStructureArray(companies)),
      );
  }

  private mapDtoToCompanyUnitStructure(structureDto: StructureDto): Structure {
    if (structureDto.substructure.length === 0) {
      return;
    }

    return {
      id: structureDto.okrUnitId,
      name: structureDto.unitName,
      label: structureDto.label,
      objectives: structureDto.objectiveIds,
      substructures: this.mapDtosToStructureArray(structureDto.substructure),
    };
  }

  private mapDtosToStructureArray(structureDtos: StructureDto[]): Structure[] {
    const companyUnitStructures: Structure[] = [];
    if (structureDtos) {
      for (const dto of structureDtos) {
        companyUnitStructures.push(this.mapDtoToCompanyUnitStructure(dto));
      }
    }

    return companyUnitStructures;
  }
}
