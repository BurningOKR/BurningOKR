import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { DepartmentUnit } from '../../model/ui/OrganizationalUnit/department-unit';
import { DepartmentApiService } from '../api/department-api.service';
import { DepartmentDto } from '../../model/api/department.dto';
import { CompanyId, DepartmentId } from '../../model/id-types';

@Injectable({
  providedIn: 'root'
})
export class DepartmentMapper {
  constructor(private departmentApiService: DepartmentApiService) {
  }

  static mapDepartment(department: DepartmentDto): DepartmentUnit {
    return new DepartmentUnit(
      department.structureId,
      department.structureName,
      department.subDepartmentIds,
      department.objectiveIds,
      department.parentStructureId,
      department.label,
      department.okrMasterId,
      department.okrTopicSponsorId,
      department.okrMemberIds,
      department.isActive,
      department.isParentStructureADepartment
    );
  }

  getDepartmentById$(departmentId: DepartmentId): Observable<DepartmentUnit> {
    return this.departmentApiService
      .getDepartmentById$(departmentId)
      .pipe(map(departmentDto => DepartmentMapper.mapDepartment(departmentDto)));
  }

  getDepartmentsForCompany$(companyId: CompanyId): Observable<DepartmentUnit[]> {
    return this.departmentApiService
      .getDepartmentsForCompany$(companyId)
      .pipe(map((departments: DepartmentDto[]) => departments.map(DepartmentMapper.mapDepartment)));
  }

  getAllDepartmentsForCompanyFlatted$(companyId: CompanyId): Observable<DepartmentUnit[]> {
    return this.departmentApiService
      .getDepartmentsFlattedForCompany$(companyId)
      .pipe(map((departments: DepartmentDto[]) => departments.map(DepartmentMapper.mapDepartment)
        .sort((a: DepartmentUnit, b: DepartmentUnit) => a.name < b.name ? -1 : a.name === b.name ? 0 : 1
        )));
  }

  getDepartmentsForDepartment$(departmentId: DepartmentId): Observable<DepartmentUnit[]> {
    return this.departmentApiService
      .getDepartmentsForDepartment$(departmentId)
      .pipe(map((departments: DepartmentDto[]) => departments.map(DepartmentMapper.mapDepartment)));
  }

  postDepartmentForCompany$(companyId: CompanyId, department: DepartmentDto): Observable<DepartmentUnit> {
    return this.departmentApiService.postDepartmentForCompany$(companyId, department)
      .pipe(map(DepartmentMapper.mapDepartment));
  }

  postDepartmentForDepartment$(departmentId: DepartmentId, department: DepartmentDto): Observable<DepartmentUnit> {
    return this.departmentApiService
      .postDepartmentForDepartment$(departmentId, department)
      .pipe(map(DepartmentMapper.mapDepartment));
  }

  putDepartment$(department: DepartmentUnit): Observable<DepartmentUnit> {
    return this.departmentApiService
      .putDepartment$({
        structureId: department.id,
        structureName: department.name,
        subDepartmentIds: department.subDepartmentIds,
        objectiveIds: department.objectives,
        parentStructureId: 0,
        label: department.label,
        okrMasterId: department.okrMasterId,
        okrTopicSponsorId: department.okrTopicSponsorId,
        okrMemberIds: department.okrMemberIds,
        isActive: department.isActive
      })
      .pipe(map(DepartmentMapper.mapDepartment));
  }

  putDepartmentObjectiveSequence$(departmentId: DepartmentId, sequenceList: number[]): Observable<number[]> {
    return this.departmentApiService.putDepartmentObjectiveSequence$(departmentId, sequenceList);
  }

  deleteDepartment$(departmentId: DepartmentId): Observable<boolean> {
    return this.departmentApiService.deleteDepartment$(departmentId);
  }
}
