import { Injectable } from '@angular/core';
import { OkrChildUnit } from '../../model/ui/OrganizationalUnit/okr-child-unit';
import { BehaviorSubject, Observable } from 'rxjs';
import { OkrUnitId } from '../../model/id-types';
import { OkrUnitApiService } from '../api/okr-unit-api.service';
import { OkrDepartment } from '../../model/ui/OrganizationalUnit/okr-department';
import { OkrBranch } from '../../model/ui/OrganizationalUnit/okr-branch';
import { DepartmentMapper } from './department.mapper';
import { OkrBranchMapper } from './okr-branch-mapper.service';
import { OkrBranchDto } from '../../model/api/OkrUnit/okr-branch.dto';
import { map, switchMap } from 'rxjs/operators';
import { OkrDepartmentDto } from '../../model/api/OkrUnit/okr-department.dto';
import { OkrChildUnitDto } from '../../model/api/OkrUnit/okr-child-unit.dto';

@Injectable({
  providedIn: 'root'
})
export class OkrUnitService {

  private refresh$: BehaviorSubject<null> = new BehaviorSubject<null>(null);

  constructor(private okrUnitApiService: OkrUnitApiService) {
  }

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

  refreshOkrChildUnit(): void {
    this.refresh$.next(null);
  }

  getOkrChildUnitById$(id: OkrUnitId, handleErrors: boolean = true): Observable<OkrChildUnit> {
    return this.refresh$
      .pipe(
        switchMap(() => {
          return this.okrUnitApiService.getOkrChildUnitById$(id, handleErrors)
            .pipe(
              map((okrChildUnit: OkrChildUnitDto) => OkrUnitService.mapToEntity(okrChildUnit))
            );
        })
      );

  }

  putOkrChildUnit$(okrChildUnit: OkrChildUnit, handleErrors: boolean = true): Observable<OkrChildUnit> {
    return this.okrUnitApiService.putOkrChildUnit$(okrChildUnit.id, OkrUnitService.mapToDto(okrChildUnit), handleErrors)
      .pipe(
        map((childUnit: OkrChildUnitDto) => OkrUnitService.mapToEntity(childUnit))
      );
  }

  deleteOkrChildUnit$(okrChildUnit: OkrChildUnit, handleErrors: boolean = true): Observable<boolean> {
    return this.okrUnitApiService.deleteOkrChildUnit$(okrChildUnit.id, handleErrors);
  }

  putOkrUnitObjectiveSequence$(okrUnitId: OkrUnitId, sequenceList: number[], handleErrors: boolean = true): Observable<number[]> {
    return this.okrUnitApiService.putOkrUnitObjectiveSequence$(okrUnitId, sequenceList, handleErrors);
  }
}
