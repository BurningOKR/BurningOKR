import { CompanyId, CycleId } from '../model/id-types';
import { Observable, of } from 'rxjs';
import { CycleState, CycleUnit } from '../model/ui/cycle-unit';
import { CycleDto } from '../model/api/cycle.dto';

export class CycleMapperMock {
  getCycleById$(id: CycleId): Observable<CycleUnit> {
    return of();
  }

  getCyclesOfCompany$(companyId: CompanyId): Observable<CycleUnit[]> {
    return of();
  }

  getAllCycles$(): Observable<CycleUnit[]> {
    return of();
  }

  postCycle$(cycleUnit: CycleUnit): Observable<CycleUnit> {
    return of();
  }

  putCycle$(cycleUnit: CycleUnit): Observable<CycleUnit> {
    return of();
  }

  cloneCycleFromCycleId$(formerCycleId: number, cycleUnit: CycleUnit): Observable<CycleUnit> {
    return of();
  }

  mapCycleList(cycleList: CycleDto[]): CycleUnit[] {
    return [];
  }

  deleteCycleById$(cycleId: number): Observable<boolean> {
    return of();
  }

  mapCycleToCycleUnit(cycle: CycleDto): CycleUnit {
    return new CycleUnit(
      undefined,
      '',
      [],
      undefined,
      undefined,
      undefined,
      false);
  }

  mapCycleUnitToCycle(cycleUnit: CycleUnit): CycleDto {
    return {
      name: '',
      companyIds : [],
      plannedStartDate : [],
      plannedEndDate : [],
      cycleState : CycleState.CLOSED,
      isVisible : true
    };
  }
}
