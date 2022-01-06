import {Injectable} from '@angular/core';
import {StructureApiService} from '../api/structure-api.service';
import {Observable} from 'rxjs';
import {Structure} from '../../model/ui/OrganizationalUnit/structure';
import {StructureDto} from '../../model/api/OkrUnit/structure.dto';
import {map} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class StructureMapper {

  constructor(
    private structureApiService: StructureApiService
  ) {}

  getSchemaOfAllExistingStructures$(): Observable<Structure[]>{
    return this.structureApiService
      .getSchemaOfAllExistingStructures$().pipe(
        map(companies => this.mapDtosToStructureArray(companies))
      );
  }

  private mapDtoToCompanyUnitStructure(structureDto: StructureDto) {
    if(structureDto.substructure === []){
      return;
    }
    return new Structure(
      structureDto.okrUnitId,
      structureDto.unitName,
      structureDto.label,
      structureDto.objectiveIds,
      this.mapDtosToStructureArray(structureDto.substructure)
    );
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
