import {Injectable} from '@angular/core';
import {OkrChildUnit} from '../../model/ui/OrganizationalUnit/okr-child-unit';
import {BehaviorSubject, Observable} from 'rxjs';
import {OkrUnitId} from '../../model/id-types';
import {OkrUnitApiService} from '../api/okr-unit-api.service';
import {DepartmentMapper} from './department.mapper';
import {OkrBranchMapper} from './okr-branch-mapper.service';
import {map, switchMap} from 'rxjs/operators';
import {OkrChildUnitDto} from '../../model/api/OkrUnit/okr-child-unit.dto';
import {UnitType} from "../../model/api/OkrUnit/unit-type.enum";
import {OkrDepartmentDto} from "../../model/api/OkrUnit/okr-department.dto";
import {OkrBranchDto} from "../../model/api/OkrUnit/okr-branch.dto";
import {OkrDepartment} from "../../model/ui/OrganizationalUnit/okr-department";
import {OkrBranch} from "../../model/ui/OrganizationalUnit/okr-branch";

@Injectable({
  providedIn: 'root',
})
export class OkrUnitService {

  private refresh$: BehaviorSubject<null> = new BehaviorSubject<null>(null);

  constructor(private okrUnitApiService: OkrUnitApiService) {
  }

  private static mapToDto(okrChildUnit: OkrChildUnit): OkrChildUnitDto {
    if (okrChildUnit.type === UnitType.DEPARTMENT) {
      return DepartmentMapper.mapDepartmentUnit(okrChildUnit as OkrDepartment);
    } else if (okrChildUnit.type === UnitType.OKR_BRANCH) {
      return OkrBranchMapper.mapToOkrBranchDto(okrChildUnit as OkrBranch);
    }
  }

  private static mapToEntity(okrChildUnit: OkrChildUnitDto): OkrChildUnit {
    if (okrChildUnit.__okrUnitType === UnitType.DEPARTMENT) {
      return DepartmentMapper.mapDepartmentDto(okrChildUnit as OkrDepartmentDto);
    } else if (okrChildUnit.__okrUnitType === UnitType.OKR_BRANCH) {
      OkrBranchMapper.mapToOkrBranch(okrChildUnit as OkrBranchDto);
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
              map((okrChildUnit: OkrChildUnitDto) => OkrUnitService.mapToEntity(okrChildUnit)),
            );
        }),
      );

  }

  putOkrChildUnit$(okrChildUnit: OkrChildUnit, handleErrors: boolean = true): Observable<OkrChildUnit> {
    return this.okrUnitApiService.putOkrChildUnit$(okrChildUnit.id, OkrUnitService.mapToDto(okrChildUnit), handleErrors)
      .pipe(
        map((childUnit: OkrChildUnitDto) => OkrUnitService.mapToEntity(childUnit)),
      );
  }

  deleteOkrChildUnit$(okrChildUnit: OkrChildUnit, handleErrors: boolean = true): Observable<boolean> {
    return this.okrUnitApiService.deleteOkrChildUnit$(okrChildUnit.id, handleErrors);
  }

  putOkrUnitObjectiveSequence$(
    okrUnitId: OkrUnitId,
    sequenceList: number[],
    handleErrors: boolean = true,
  ): Observable<number[]> {
    return this.okrUnitApiService.putOkrUnitObjectiveSequence$(okrUnitId, sequenceList, handleErrors);
  }
}
