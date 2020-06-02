import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { DepartmentUnit } from '../../model/ui/OrganizationalUnit/department-unit';
import { DepartmentApiService } from '../api/department-api.service';
import { DepartmentDto } from '../../model/api/structure/department.dto';
import { CompanyId, StructureId } from '../../model/id-types';
import { StructureType } from '../../model/api/structure/structure-type.enum';

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
      department.objectiveIds,
      department.parentStructureId,
      department.label,
      department.okrMasterId,
      department.okrTopicSponsorId,
      department.okrMemberIds,
      department.isActive,
      department.isParentStructureACorporateObjectiveStructure
    );
  }

  getDepartmentById$(departmentId: StructureId): Observable<DepartmentUnit> {
    return this.departmentApiService
      .getDepartmentById$(departmentId)
      .pipe(map(departmentDto => DepartmentMapper.mapDepartment(departmentDto)));
  }

  getAllDepartmentsForCompanyFlatted$(companyId: CompanyId): Observable<DepartmentUnit[]> {
    return this.departmentApiService
      .getDepartmentsFlattedForCompany$(companyId)
      .pipe(map((departments: DepartmentDto[]) => departments.map(DepartmentMapper.mapDepartment)
        .sort((a: DepartmentUnit, b: DepartmentUnit) => a.name < b.name ? -1 : a.name === b.name ? 0 : 1
        )));
  }

  postDepartmentForCompany$(companyId: CompanyId, department: DepartmentDto): Observable<DepartmentUnit> {
    return this.departmentApiService.postDepartmentForCompany$(companyId, department)
      .pipe(map(DepartmentMapper.mapDepartment));
  }

  postDepartmentForDepartment$(departmentId: StructureId, department: DepartmentDto): Observable<DepartmentUnit> {
    return this.departmentApiService
      .postDepartmentForDepartment$(departmentId, department)
      .pipe(map(DepartmentMapper.mapDepartment));
  }

  putDepartment$(department: DepartmentUnit): Observable<DepartmentUnit> {
    return this.departmentApiService
      .putDepartment$({
        structureId: department.id,
        structureName: department.name,
        objectiveIds: department.objectives,
        parentStructureId: 0,
        label: department.label,
        okrMasterId: department.okrMasterId,
        okrTopicSponsorId: department.okrTopicSponsorId,
        okrMemberIds: department.okrMemberIds,
        isActive: department.isActive,
        __structureType: StructureType.DEPARTMENT
      })
      .pipe(map(DepartmentMapper.mapDepartment));
  }

  putDepartmentObjectiveSequence$(departmentId: StructureId, sequenceList: number[]): Observable<number[]> {
    return this.departmentApiService.putDepartmentObjectiveSequence$(departmentId, sequenceList);
  }

  deleteDepartment$(departmentId: StructureId): Observable<boolean> {
    return this.departmentApiService.deleteDepartment$(departmentId);
  }
}
