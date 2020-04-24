import { Injectable } from '@angular/core';
import { CycleApiService } from '../api/cycle-api.service';
import { map, tap } from 'rxjs/internal/operators';
import { CycleUnit } from '../../model/ui/cycle-unit';
import { Observable, ReplaySubject } from 'rxjs';
import { CompanyApiService } from '../api/company-api.service';
import { CompanyId, CycleId } from '../../model/id-types';
import { CycleDto } from '../../model/api/cycle.dto';

@Injectable({
  providedIn: 'root'
})
export class CycleMapper {

  constructor(private cycleService: CycleApiService, private companyApiService: CompanyApiService) {
  }

  cycles: { [key: number]: ReplaySubject<CycleUnit> } = {};

  getCycleById$(id: CycleId): Observable<CycleUnit> {
    if (!this.cycles[id]) {
      this.cycles[id] = new ReplaySubject<CycleUnit>(1);
      this.cycleService.getCycleById$(id)
        .pipe(map(this.mapCycleToCycleUnit))
        .subscribe(c => {
          this.cycles[id].next(c);
          this.cycles[id].complete();
        });
    }

    return this.cycles[id].asObservable();
  }

  getCyclesOfCompany$(companyId: CompanyId): Observable<CycleUnit[]> {
    return this.companyApiService.getCyclesOfCompany$(companyId)
      .pipe(map((cycleList: CycleDto[]) => this.mapCycleList(cycleList)));
  }

  getAllCycles$(): Observable<CycleUnit[]> {
    return this.cycleService.getAllCycles$()
      .pipe(map((cycles: CycleDto[]) => {
      return cycles.map(this.mapCycleToCycleUnit)
        .sort((a: CycleUnit, b: CycleUnit) => a.name < b.name ? -1 : (a.name === b.name ? 0 : 1));
    }));
  }

  postCycle$(cycleUnit: CycleUnit): Observable<CycleUnit> {
    return this.cycleService.postCycle$(this.mapCycleUnitToCycle(cycleUnit))
      .pipe(map(this.mapCycleToCycleUnit));
  }

  putCycle$(cycleUnit: CycleUnit): Observable<CycleUnit> {
    return this.cycleService.putCycleById$(this.mapCycleUnitToCycle(cycleUnit))
      .pipe(map(this.mapCycleToCycleUnit),
      tap((cycle: CycleUnit) => {
        this.cycles[cycle.id] = new ReplaySubject<CycleUnit>(1);
        this.cycles[cycle.id].next(cycle);
        this.cycles[cycle.id].complete();
      })
    );
  }

  cloneCycleFromCycleId$(formerCycleId: number, cycleUnit: CycleUnit): Observable<CycleUnit> {
    return this.cycleService.cloneCycleFromCycleId$(formerCycleId, this.mapCycleUnitToCycle(cycleUnit))
      .pipe(map(this.mapCycleToCycleUnit));
  }

  mapCycleList(cycleList: CycleDto[]): CycleUnit[] {
    const cycleUnitList: CycleUnit[] = [];
    if (cycleList) {
      for (const cycle of cycleList) {
        cycleUnitList.push(this.mapCycleToCycleUnit(cycle));
      }
    }

    return cycleUnitList;
  }

  deleteCycleById$(cycleId: number): Observable<boolean> {
    return this.cycleService.deleteCycleById$(cycleId);
  }

  mapCycleToCycleUnit(cycle: CycleDto): CycleUnit {
    const startDate: Date = new Date(cycle.plannedStartDate[0], cycle.plannedStartDate[1] - 1, cycle.plannedStartDate[2]);
    const endDate: Date = new Date(cycle.plannedEndDate[0], cycle.plannedEndDate[1] - 1, cycle.plannedEndDate[2]);

    return new CycleUnit(
      cycle.id,
      cycle.name,
      cycle.companyIds,
      startDate,
      endDate,
      cycle.cycleState,
      cycle.isVisible
    );
  }

  mapCycleUnitToCycle(cycleUnit: CycleUnit): CycleDto {
    const startDate: number[] = [Number(cycleUnit.startDate.getFullYear()),
                                 Number(cycleUnit.startDate.getMonth()) + 1,
                                 Number(cycleUnit.startDate.getDate())];
    const endDate: number[] = [Number(cycleUnit.endDate.getFullYear()),
                               Number(cycleUnit.endDate.getMonth()) + 1,
                               Number(cycleUnit.endDate.getDate())];

    return {
      plannedStartDate: startDate,
      plannedEndDate: endDate,
      id: cycleUnit.id,
      name: cycleUnit.name,
      companyIds: cycleUnit.companyIds,
      cycleState: cycleUnit.cycleState,
      isVisible: cycleUnit.isVisible
    };
  }
}
