import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CompanyApiService } from '../api/company-api.service';
import { DepartmentApiService } from '../api/department-api.service';
import { map } from 'rxjs/operators';
import { OkrUnitSchemaDto } from '../../model/api/OkrUnit/okr-unit-schema-dto';
import { OkrUnitSchema } from '../../model/ui/okr-unit-schema';
import { CompanyId, OkrUnitId } from '../../model/id-types';
import { OkrUnitApiService } from '../api/okr-unit-api.service';

@Injectable({
  providedIn: 'root'
})
export class OkrUnitSchemaMapper {
  constructor(
    private departmentApiService: DepartmentApiService,
    private companyApiService: CompanyApiService,
    private okrUnitApiService: OkrUnitApiService) {}

  getOkrUnitSchemaOfDepartment$(departmentId: OkrUnitId): Observable<OkrUnitSchema[]> {
    return this.departmentApiService
      .getOkrUnitSchema$(departmentId)
      .pipe(map(dto => this.mapOkrUnitSchemaDtoList(dto)));
  }

  getOkrUnitSchemaOfCompany$(companyId: CompanyId): Observable<OkrUnitSchema[]> {
    return this.companyApiService
      .getOkrUnitSchemaOfCompany$(companyId)
      .pipe(map(dto => this.mapOkrUnitSchemaDtoList(dto)));
  }

  getOkrUnitSchemaByUnitId$(id: OkrUnitId): Observable<OkrUnitSchema[]> {
    return this.okrUnitApiService.getOkrUnitSchemaByUnitId$(id)
      .pipe(
        map((okrUnitSchemas: OkrUnitSchema[]) => this.mapOkrUnitSchemaDtoList(okrUnitSchemas))
      );
  }

  mapOkrUnitSchemaDtoList(dtoList: OkrUnitSchemaDto[]): OkrUnitSchema[] {
    const okrUnitSchemas: OkrUnitSchema[] = [];
    if (dtoList) {
      for (const dto of dtoList) {
        okrUnitSchemas.push(this.mapOkrUnitSchemaDto(dto));
      }
    }
    okrUnitSchemas.sort(this.sortOkrUnitSchema);

    return okrUnitSchemas;
  }

  sortOkrUnitSchema(a: OkrUnitSchema, b: OkrUnitSchema): number {
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

  mapOkrUnitSchemaDto(dto: OkrUnitSchemaDto): OkrUnitSchema {
    const okrUnitSchema: OkrUnitSchema = new OkrUnitSchema(dto.id, dto.name.toString(), dto.userRole, dto.isActive);
    okrUnitSchema.subDepartments = this.mapOkrUnitSchemaDtoList(dto.subDepartments);

    return okrUnitSchema;
  }
}
