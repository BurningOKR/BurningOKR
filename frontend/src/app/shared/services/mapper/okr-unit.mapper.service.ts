import { Injectable } from '@angular/core';
import { OkrChildUnit } from '../../model/ui/OrganizationalUnit/okr-child-unit';
import { Observable } from 'rxjs';
import { OkrUnitId } from '../../model/id-types';
import { OkrUnitApiService } from '../api/okr-unit-api.service';
import { OkrDepartment } from '../../model/ui/OrganizationalUnit/okr-department';
import { OkrBranch } from '../../model/ui/OrganizationalUnit/okr-branch';
import { DepartmentMapper } from './department.mapper';
import { OkrBranchMapper } from './okr-branch-mapper.service';
import { OkrBranchDto } from '../../model/api/OkrUnit/okr-branch.dto';
import { map } from 'rxjs/operators';
import { OkrDepartmentDto } from '../../model/api/OkrUnit/okr-department.dto';
import { OkrChildUnitDto } from '../../model/api/OkrUnit/okr-child-unit.dto';

@Injectable({
  providedIn: 'root'
})
export class OkrUnitMapper {

  constructor(private okrUnitApiService: OkrUnitApiService) { }

  private static mapToDto(okrChildUnit: OkrChildUnit): OkrChildUnitDto {
    if (okrChildUnit instanceof OkrDepartment) {
      return DepartmentMapper.mapDepartmentUnit(okrChildUnit);
    } else if (okrChildUnit instanceof OkrBranch) {
      return OkrBranchMapper.mapToOkrBranchDto(okrChildUnit);
    }
  }

  private static mapToEntity(okrChildUnit: OkrChildUnitDto): OkrChildUnit {
    if (okrChildUnit instanceof OkrDepartmentDto) {
      return DepartmentMapper.mapDepartmentDto(okrChildUnit);
    } else if (okrChildUnit instanceof OkrBranchDto) {
      return OkrBranchMapper.mapToOkrBranch(okrChildUnit);
    }
  }

  getOkrChildUnitById$(id: OkrUnitId): Observable<OkrChildUnit> {
    return this.okrUnitApiService.getOkrChildUnitById$(id)
      .pipe(
        map((okrChildUnit: OkrChildUnitDto) => OkrUnitMapper.mapToEntity(okrChildUnit))
      );
  }

  putOkrChildUnit$(okrChildUnit: OkrChildUnit): Observable<OkrChildUnit> {
    return this.okrUnitApiService.putOkrChildUnit$(okrChildUnit.id, OkrUnitMapper.mapToDto(okrChildUnit))
      .pipe(
        map((childUnit: OkrChildUnitDto) => OkrUnitMapper.mapToEntity(childUnit))
      );
  }

  deleteOkrChildUnit$(okrChildUnit: OkrChildUnit): Observable<boolean> {
    return this.okrUnitApiService.deleteOkrChildUnit$(okrChildUnit.id);
  }

  putOkrUnitObjectiveSequence$(okrUnitId: OkrUnitId, sequenceList: number[]): Observable<number[]> {
    return this.okrUnitApiService.putOkrUnitObjectiveSequence$(okrUnitId, sequenceList);
  }
}
