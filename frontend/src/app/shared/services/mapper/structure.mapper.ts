import {Injectable} from "@angular/core";
import {StructureApiService} from "../api/structure-api.service";
import {Observable} from "rxjs";
import {Structure} from "../../model/ui/OrganizationalUnit/structure";
import {StructureDto} from "../../model/api/OkrUnit/structure.dto";
import {map} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class StructureMapper {

  constructor(
    private structureApiService: StructureApiService
  )
  {}

  getSchemaOfAllExistingStructures$(): Observable<Structure[]>{
    return this.structureApiService
      .getSchemaOfAllExistingStructures$().pipe(
        map(companies => this.mapDtosToCompanyUnitStructureList(companies))
      )
  }

  private mapDtoToCompanyUnitStructure(structureDto :StructureDto) {
    console.log(structureDto)
    const companyUnitStructure: Structure = new Structure(
      structureDto.okrUnitId,
      structureDto.unitName,
      structureDto.okrChildUnitIds,
      structureDto.objectiveIds,
      structureDto.cycleId,
      structureDto.label,
      structureDto.unitSchema
    )

    console.log(companyUnitStructure)
    return companyUnitStructure;
  }

  private mapDtosToCompanyUnitStructureList(structureDtos: StructureDto[]): Structure[]
  {
    const companyUnitStructures: Structure[] = [];
    if (structureDtos) {
      for (const dto of structureDtos) {
        companyUnitStructures.push(this.mapDtoToCompanyUnitStructure(dto));
      }
    }
    return companyUnitStructures;
  }
}
