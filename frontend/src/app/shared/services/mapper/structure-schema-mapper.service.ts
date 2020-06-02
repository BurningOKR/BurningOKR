import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CompanyApiService } from '../api/company-api.service';
import { DepartmentApiService } from '../api/department-api.service';
import { map } from 'rxjs/operators';
import { StructureSchemeDto } from '../../model/api/structure-scheme-dto';
import { StructureSchema } from '../../model/ui/structure-schema';
import { CompanyId, DepartmentId } from '../../model/id-types';

@Injectable({
  providedIn: 'root'
})
export class StructureSchemaMapper {
  constructor(private departmentApiService: DepartmentApiService, private companyApiService: CompanyApiService) {}

  getStructureSchemaOfDepartment$(departmentId: DepartmentId): Observable<StructureSchema[]> {
    return this.departmentApiService
      .getStructureSchema$(departmentId)
      .pipe(map(dto => this.mapStructureSchemaDtoList(dto)));
  }

  getStructureSchemaOfCompany$(companyId: CompanyId): Observable<StructureSchema[]> {
    return this.companyApiService
      .getStructureSchemaOfCompany$(companyId)
      .pipe(map(dto => this.mapStructureSchemaDtoList(dto)));
  }

  mapStructureSchemaDtoList(dtoList: StructureSchemeDto[]): StructureSchema[] {
    const structureSchemaList: StructureSchema[] = [];
    if (dtoList) {
      for (const dto of dtoList) {
        structureSchemaList.push(this.mapStructureSchemaDto(dto));
      }
    }
    structureSchemaList.sort(this.sortStructureSchema);

    return structureSchemaList;
  }

  sortStructureSchema(a: StructureSchema, b: StructureSchema): number {
    if (a.isActive > b.isActive) {
      return -1;
    }
    if (b.isActive > a.isActive) {
      return 1;
    }
    if (a.name.toLocaleLowerCase() > b.name.toLocaleLowerCase()) {
      return 1;
    } else {
      return -1;
    }
  }

  mapStructureSchemaDto(dto: StructureSchemeDto): StructureSchema {
    const structureSchema: StructureSchema = new StructureSchema(dto.id, dto.name.toString(), dto.userRole, dto.isActive);
    structureSchema.subDepartments = this.mapStructureSchemaDtoList(dto.subDepartments);

    return structureSchema;
  }
}
