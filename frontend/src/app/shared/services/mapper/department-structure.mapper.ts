import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CompanyApiService } from '../api/company-api.service';
import { DepartmentApiService } from '../api/department-api.service';
import { map } from 'rxjs/operators';
import { DepartmentStructureDto } from '../../model/api/department-structure.dto';
import { DepartmentStructure } from '../../model/ui/department-structure';
import { CompanyId, DepartmentId } from '../../model/id-types';

@Injectable({
  providedIn: 'root'
})
export class DepartmentStructureMapper {
  constructor(private departmentApiService: DepartmentApiService, private companyApiService: CompanyApiService) {}

  getDepartmentStructuresOfDepartment$(departmentId: DepartmentId): Observable<DepartmentStructure[]> {
    return this.departmentApiService
      .getDepartmentStructure$(departmentId)
      .pipe(map(dto => this.mapDepartmentStructureDtoList(dto)));
  }

  getDepartmentStructuresOfCompany$(companyId: CompanyId): Observable<DepartmentStructure[]> {
    return this.companyApiService
      .getDepartmentStructuresOfCompany$(companyId)
      .pipe(map(dto => this.mapDepartmentStructureDtoList(dto)));
  }

  mapDepartmentStructureDtoList(dtoList: DepartmentStructureDto[]): DepartmentStructure[] {
    const departmentStructures: DepartmentStructure[] = [];
    if (dtoList) {
      for (const dto of dtoList) {
        departmentStructures.push(this.mapDepartmentStructureDto(dto));
      }
    }
    departmentStructures.sort(this.sortDepartmentStructure);

    return departmentStructures;
  }

  sortDepartmentStructure(a: DepartmentStructure, b: DepartmentStructure): number {
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

  mapDepartmentStructureDto(dto: DepartmentStructureDto): DepartmentStructure {
    const departmentStructure: DepartmentStructure = new DepartmentStructure(dto.id, dto.name.toString(), dto.userRole, dto.isActive);
    departmentStructure.subDepartments = this.mapDepartmentStructureDtoList(dto.subDepartments);

    return departmentStructure;
  }
}
