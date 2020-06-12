// istanbul ignore file
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { OkrUnitSchemaDto } from '../../model/api/OkrUnit/okr-unit-schema-dto';
import { ApiHttpService } from '../../../core/services/api-http.service';
import { CompanyId, OkrUnitId } from '../../model/id-types';
import { OkrDepartmentDto } from '../../model/api/OkrUnit/okrDepartment.dto';
import { CompanyDto } from '../../model/api/OkrUnit/company.dto';

@Injectable({
  providedIn: 'root'
})
export class DepartmentApiService {
  constructor(private api: ApiHttpService) {}

  getDepartmentById$(id: OkrUnitId): Observable<OkrDepartmentDto> {
    return this.api.getData$<OkrDepartmentDto>(`departments/${id}`);
  }

  getParentCompanyOfDepartment$(departmentId: OkrUnitId): Observable<CompanyDto> {
    return this.api.getData$(`departments/${departmentId}/company`);
  }

  getOkrUnitSchema$(departmentId: OkrUnitId): Observable<OkrUnitSchemaDto[]> {
    return this.api.getData$(`departments/${departmentId}/schema`);
  }

  postDepartmentForCompany$(companyId: CompanyId, department: OkrDepartmentDto): Observable<OkrDepartmentDto> {
    return this.api.postData$(`companies/${companyId}/departments`, department);
  }

  postDepartmentForOkrBranch$(okrUnitId: OkrUnitId, department: OkrDepartmentDto): Observable<OkrDepartmentDto> {
    return this.api.postData$(`branch/${okrUnitId}/department`, department);
  }

  putDepartment$(department: OkrDepartmentDto): Observable<OkrDepartmentDto> {
    return this.api.putData$(`departments/${department.okrUnitId}`, department);
  }

  deleteDepartment$(departmentId: number): Observable<boolean> {
    return this.api.deleteData$(`departments/${departmentId}`);
  }
  getDepartmentsFlattedForCompany$(companyId: number): Observable<OkrDepartmentDto[]> {
    return this.api.getData$(`departments/flatted/${companyId}`);
  }
}
