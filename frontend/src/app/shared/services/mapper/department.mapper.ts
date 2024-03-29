import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { OkrDepartment } from '../../model/ui/OrganizationalUnit/okr-department';
import { DepartmentApiService } from '../api/department-api.service';
import { OkrDepartmentDto } from '../../model/api/OkrUnit/okr-department.dto';
import { CompanyId, OkrUnitId } from '../../model/id-types';
import { UnitType } from '../../model/api/OkrUnit/unit-type.enum';

@Injectable({
  providedIn: 'root',
})
export class DepartmentMapper {
  constructor(private departmentApiService: DepartmentApiService) {
  }

  static mapDepartmentDto(department: OkrDepartmentDto): OkrDepartment {
    return {
      type: UnitType.DEPARTMENT,
      id: department.okrUnitId,
      name: department.unitName,
      objectives: department.objectiveIds,
      parentUnitId: department.parentUnitId,
      label: department.label,
      okrMasterId: department.okrMasterId,
      okrTopicSponsorId: department.okrTopicSponsorId,
      okrMemberIds: department.okrMemberIds,
      isActive: department.isActive,
      isParentUnitABranch: department.isParentUnitABranch,
    };
  }

  static mapDepartmentUnit(department: OkrDepartment): OkrDepartmentDto {
    return {
      __okrUnitType: UnitType.DEPARTMENT,
      okrUnitId: department.id,
      unitName: department.name,
      label: department.label,
      isActive: department.isActive,
      parentUnitId: department.parentUnitId,
      okrMasterId: department.okrMasterId,
      okrMemberIds: department.okrMemberIds,
      okrTopicSponsorId: department.okrTopicSponsorId,
      objectiveIds: department.objectives,
      isParentUnitABranch: department.isParentUnitABranch,
    };
  }

  getDepartmentById$(departmentId: OkrUnitId): Observable<OkrDepartment> {
    return this.departmentApiService
      .getDepartmentById$(departmentId)
      .pipe(map(departmentDto => DepartmentMapper.mapDepartmentDto(departmentDto)));
  }

  getAllDepartmentsForCompanyFlatted$(companyId: CompanyId): Observable<OkrDepartment[]> {
    return this.departmentApiService
      .getDepartmentsFlattedForCompany$(companyId)
      .pipe(map((departments: OkrDepartmentDto[]) => departments.map(DepartmentMapper.mapDepartmentDto)
        .sort((a: OkrDepartment, b: OkrDepartment) => a.name < b.name ? -1 : a.name === b.name ? 0 : 1,
        )));
  }

  postDepartmentForCompany$(companyId: CompanyId, department: OkrDepartmentDto): Observable<OkrDepartment> {
    return this.departmentApiService.postDepartmentForCompany$(companyId, department)
      .pipe(map(DepartmentMapper.mapDepartmentDto));
  }

  postDepartmentForOkrBranch$(branchId: OkrUnitId, department: OkrDepartmentDto): Observable<OkrDepartment> {
    return this.departmentApiService
      .postDepartmentForOkrBranch$(branchId, department)
      .pipe(map(DepartmentMapper.mapDepartmentDto));
  }

  putDepartment$(department: OkrDepartment): Observable<OkrDepartment> {
    return this.departmentApiService
      .putDepartment$(DepartmentMapper.mapDepartmentUnit(department))
      .pipe(map(DepartmentMapper.mapDepartmentDto));
  }

  deleteDepartment$(departmentId: OkrUnitId): Observable<boolean> {
    return this.departmentApiService.deleteDepartment$(departmentId);
  }
}
