import { Injectable } from '@angular/core';
import { ApiHttpService } from '../../../core/services/api-http.service';
import { Observable } from 'rxjs';
import { SubStructureDto } from '../../model/api/structure/sub-structure.dto';
import { StructureSchema } from '../../model/ui/structure-schema';
import { map } from 'rxjs/operators';
import { plainToClass } from 'class-transformer';
import { DepartmentDto } from '../../model/api/structure/department.dto';
import { CorporateObjectiveStructureDto } from '../../model/api/structure/corporate-objective-structure.dto';
import { CompanyDto } from '../../model/api/structure/company.dto';

@Injectable({
  providedIn: 'root'
})
export class StructureApiService {

  constructor(private http: ApiHttpService) { }

  getSubStructureById$(structureId: number): Observable<SubStructureDto> {
    return this.http.getData$<SubStructureDto>(`structures/${structureId}`)
      .pipe(
        map((value: SubStructureDto) => {
          if (value.__structureType === 'DEPARTMENT') {
            return plainToClass(DepartmentDto, value);
          } else if (value.__structureType === 'CORPORATE_OBJECTIVE_STRUCTURE') {
            return plainToClass(CorporateObjectiveStructureDto, value);
          }
        })
      );
  }

  putSubStructure$(structureId: number, subStructure: SubStructureDto): Observable<SubStructureDto> {
    return this.http.putData$<SubStructureDto>(`structures/${structureId}`, subStructure);
  }

  deleteSubStructure$(structureId: number): Observable<boolean> {
    return this.http.deleteData$(`structures/${structureId}`);
  }

  getStructureSchemaByStructureId$(structureId: number): Observable<StructureSchema[]> {
    return this.http.getData$<StructureSchema[]>(`structures/${structureId}/structure`);
  }

  getParentCompanyOfStructure$(structureId: number): Observable<CompanyDto> {
    return this.http.getData$(`structures/${structureId}/company`);
  }

  putStructureObjectiveSequence$(departmentId: number, sequenceList: number[]): Observable<number[]> {
    return this.http.putData$(`structures/${departmentId}/objectivesequence`, sequenceList);
  }
}
