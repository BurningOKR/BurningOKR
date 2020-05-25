// istanbul ignore file
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { DepartmentStructureDto } from '../../model/api/department-structure.dto';
import { ApiHttpService } from '../../../core/services/api-http.service';
import { CompanyId, DepartmentId } from '../../model/id-types';
import { DepartmentDto } from '../../model/api/department.dto';
import { CompanyDto } from '../../model/api/company.dto';

@Injectable({
  providedIn: 'root'
})
export class DepartmentApiService {
  constructor(private api: ApiHttpService) {}

  getDepartmentById$(id: DepartmentId): Observable<DepartmentDto> {
    return this.api.getData$<DepartmentDto>(`departments/${id}`);
  }

  getDepartmentsForCompany$(companyId: CompanyId): Observable<DepartmentDto[]> {
    return this.api.getData$(`companies/${companyId}/departments`);
  }

  getParentCompanyOfDepartment$(departmentId: DepartmentId): Observable<CompanyDto> {
    return this.api.getData$(`departments/${departmentId}/company`);
  }

  getDepartmentsForDepartment$(departmentId: DepartmentId): Observable<DepartmentDto[]> {
    return this.api.getData$(`departments/${departmentId}/departments`);
  }

  getDepartmentStructure$(departmentId: DepartmentId): Observable<DepartmentStructureDto[]> {
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
