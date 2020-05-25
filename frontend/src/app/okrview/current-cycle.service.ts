import { Injectable } from '@angular/core';
import { NEVER, Observable, ReplaySubject } from 'rxjs';
import { CycleUnit } from '../shared/model/ui/cycle-unit';
import { CurrentCompanyService } from './current-company.service';
import { CycleMapper } from '../shared/services/mapper/cycle.mapper';
import { map, switchMap } from 'rxjs/operators';
import { CompanyUnit } from '../shared/model/ui/OrganizationalUnit/company-unit';

interface CycleListWithCompany {
  company: CompanyUnit;
  cycleList: CycleUnit[];
}

@Injectable({
  providedIn: 'root'
})
export class CurrentCycleService {

  private currentCycle$: ReplaySubject<CycleUnit> = new ReplaySubject<CycleUnit>();
  private currentCycleList$: ReplaySubject<CycleUnit[]> = new ReplaySubject<CycleUnit[]>();

  constructor(private currentCompanyService: CurrentCompanyService,
              private cycleMapperService: CycleMapper) {

    this.currentCompanyService.getCurrentCompany$()
      .pipe(
        switchMap((currentCompany: CompanyUnit) => {
          if (!!currentCompany) {
            return this.cycleMapperService.getCyclesOfCompany$(currentCompany.id)
              .pipe(
                map((cycleList: CycleUnit[]) => {
                  return {
                    cycleList,
                    company: currentCompany
                  };
                })
              );
          } else {
            return NEVER;
          }
        })
      )
      .subscribe((cycleListWithCompany: CycleListWithCompany) => {
        this.currentCycleList$.next(cycleListWithCompany.cycleList);
        this.setCurrentCycleFromList(cycleListWithCompany.cycleList, cycleListWithCompany.company.id);
      });
  }

  getCurrentCycleList$(): Observable<CycleUnit[]> {
    return this.currentCycleList$.asObservable();
  }

  getCurrentCycle$(): Observable<CycleUnit> {
    return this.currentCycle$.asObservable();
  }

  private setCurrentCycleFromList(cycleList: CycleUnit[], companyId: number): void {
    for (const cycle of cycleList) {
      if (cycle.companyIds.includes(companyId)) {
        this.currentCycle$.next(cycle);
        break;
      }
    }
  }
}
