// istanbul ignore file
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { StructureSchemeDto } from '../../model/api/structure/structure-scheme-dto';
import { ApiHttpService } from '../../../core/services/api-http.service';
import { CompanyId, StructureId } from '../../model/id-types';
import { DepartmentDto } from '../../model/api/structure/department.dto';
import { CompanyDto } from '../../model/api/structure/company.dto';

@Injectable({
  providedIn: 'root'
})
export class DepartmentApiService {
  constructor(private api: ApiHttpService) {}

  getDepartmentById$(id: StructureId): Observable<DepartmentDto> {
    return this.api.getData$<DepartmentDto>(`departments/${id}`);
  }

  getParentCompanyOfDepartment$(departmentId: StructureId): Observable<CompanyDto> {
    return this.api.getData$(`departments/${departmentId}/company`);
  }

  getStructureSchema$(departmentId: StructureId): Observable<StructureSchemeDto[]> {
    return this.api.getData$(`departments/${departmentId}/structure`);
  }

  postDepartmentForCompany$(companyId: CompanyId, department: DepartmentDto): Observable<DepartmentDto> {
    return this.api.postData$(`companies/${companyId}/departments`, department);
  }

  postDepartmentForDepartment$(departmentId: number, department: DepartmentDto): Observable<DepartmentDto> {
    return this.api.postData$(`departments/${departmentId}/subdepartments`, department);
  }

  putDepartment$(department: DepartmentDto): Observable<DepartmentDto> {
    return this.api.putData$(`departments/${department.structureId}`, department);
  }

  putDepartmentObjectiveSequence$(departmentId: number, sequenceList: number[]): Observable<number[]> {
    return this.api.putData$(`department/${departmentId}/objectivesequence`, sequenceList);
  }

  deleteDepartment$(departmentId: number): Observable<boolean> {
    return this.api.deleteData$(`departments/${departmentId}`);
  }
  getDepartmentsFlattedForCompany$(companyId: number): Observable<DepartmentDto[]> {
    return this.api.getData$(`departments/flatted/${companyId}`);
  }
}
