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

  static mapDepartmentDto(department: DepartmentDto): DepartmentUnit {
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

  static mapDepartmentUnit(department: DepartmentUnit): DepartmentDto {
    const departmentDto: DepartmentDto = new DepartmentDto();
    departmentDto.__structureType = StructureType.DEPARTMENT;
    departmentDto.structureId = department.id;
    departmentDto.structureName = department.name;
    departmentDto.label = department.label;
    departmentDto.isActive = department.isActive;
    departmentDto.parentStructureId = department.parentStructureId;
    departmentDto.okrMasterId = department.okrMasterId;
    departmentDto.okrMemberIds = department.okrMemberIds;
    departmentDto.okrTopicSponsorId = department.okrTopicSponsorId;
    departmentDto.objectiveIds = department.objectives;
    departmentDto.isParentStructureACorporateObjectiveStructure = department.isParentStructureACorporateObjectiveStructure;

    return departmentDto;
  }

  getDepartmentById$(departmentId: StructureId): Observable<DepartmentUnit> {
    return this.departmentApiService
      .getDepartmentById$(departmentId)
      .pipe(map(departmentDto => DepartmentMapper.mapDepartmentDto(departmentDto)));
  }

  getAllDepartmentsForCompanyFlatted$(companyId: CompanyId): Observable<DepartmentUnit[]> {
    return this.departmentApiService
      .getDepartmentsFlattedForCompany$(companyId)
      .pipe(map((departments: DepartmentDto[]) => departments.map(DepartmentMapper.mapDepartmentDto)
        .sort((a: DepartmentUnit, b: DepartmentUnit) => a.name < b.name ? -1 : a.name === b.name ? 0 : 1
        )));
  }

  postDepartmentForCompany$(companyId: CompanyId, department: DepartmentDto): Observable<DepartmentUnit> {
    return this.departmentApiService.postDepartmentForCompany$(companyId, department)
      .pipe(map(DepartmentMapper.mapDepartmentDto));
  }

  postDepartmentForCorporateObjectiveStructure$(structureId: StructureId, department: DepartmentDto): Observable<DepartmentUnit> {
    return this.departmentApiService
      .postDepartmentForCorporateObjectiveStructure$(structureId, department)
      .pipe(map(DepartmentMapper.mapDepartmentDto));
  }

  putDepartment$(department: DepartmentUnit): Observable<DepartmentUnit> {
    return this.departmentApiService
      .putDepartment$(DepartmentMapper.mapDepartmentUnit(department))
      .pipe(map(DepartmentMapper.mapDepartmentDto));
  }

  deleteDepartment$(departmentId: StructureId): Observable<boolean> {
    return this.departmentApiService.deleteDepartment$(departmentId);
  }
}
