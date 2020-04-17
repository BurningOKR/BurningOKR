import { Injectable } from '@angular/core';
import { Observable, ReplaySubject, Subscription } from 'rxjs';
import { CompanyMapper } from '../shared/services/mapper/company.mapper';
import { CycleUnit } from '../shared/model/ui/cycle-unit';
import { take } from 'rxjs/operators';
import { CycleMapper } from '../shared/services/mapper/cycle.mapper';
import { DepartmentStructureMapper } from '../shared/services/mapper/department-structure.mapper';
import { CurrentDepartmentStructureService } from './current-department-structure.service';
import { CurrentNavigationService } from './current-navigation.service';
import { CurrentCompanyService } from './current-company.service';

@Injectable({
  providedIn: 'root'
})
export class CurrentOkrviewService {
  companySubscription: Subscription;
  departmentStructureSubscription: Subscription;
  cycleListSubscription: Subscription;

  // currentCompanyId: number;
  // currentDepartmentId: number;
  //
  // currentCompany: CompanyUnit;
  // currentDepartmentStructure: DepartmentStructure[];
  currentCycle$ = new ReplaySubject<CycleUnit>();
  currentCycleList$ = new ReplaySubject<CycleUnit[]>();

  constructor(
    private departmentStructureMapperService: DepartmentStructureMapper,
    private companyMappersService: CompanyMapper,
    private cycleMapperService: CycleMapper,
    private currentDepartmentStructureService: CurrentDepartmentStructureService,
    private currentNavigationService: CurrentNavigationService,
    private currentCompanyService: CurrentCompanyService
  ) {}

  browseCompany(companyId: number): void {
    this.currentNavigationService.clearDepartmentNavigationInformation();
    // if (this.currentCompanyId === companyId) {
    //   return;
    // }
    // this.currentCompanyId = companyId;
    // this.currentDepartmentId = undefined;
    this.fetchNewValuesForCompanyId(companyId);
  }

  browseDepartment(departmentId: number): void {
    // if (this.currentDepartmentId === departmentId) {
    //   return;
    // }
    // this.currentDepartmentId = departmentId;
    this.fetchNewValuesForDepartmentId(departmentId);
  }

  clearSubscriptions(): void {
    if (this.companySubscription) {
      this.companySubscription.unsubscribe();
    }
    if (this.departmentStructureSubscription) {
      this.departmentStructureSubscription.unsubscribe();
    }
    if (this.cycleListSubscription) {
      this.cycleListSubscription.unsubscribe();
    }
  }

  refreshCurrentDepartmentView(departmentId: number): void {
    this.clearSubscriptions();
    this.fetchNewValuesForDepartmentId(departmentId);
  }

  refreshCurrentCompanyView(companyId: number): void {
    this.clearSubscriptions();
    this.fetchNewValuesForCompanyId(companyId);
  }

  getCurrentCycleList$(): Observable<CycleUnit[]> {
    return this.currentCycleList$;
  }

  getCurrentCycle$(): Observable<CycleUnit> {
    return this.currentCycle$;
  }

  private fetchNewValuesForCompanyId(companyId: number): void {
    this.currentNavigationService.clearDepartmentNavigationInformation();
    this.currentCompanyService.setCurrentCompanyByCompanyId(companyId);

    // this.currentCompanyId = companyId;

    this.currentDepartmentStructureService.setCurrentDepartmentStructureByCompanyId(companyId);
    this.fetchNewCycleListForCompanyId(companyId);
  }

  private fetchNewValuesForDepartmentId(departmentId: number): void {
    this.currentCompanyService.setCurrentCompanyByChildDepartmentId(departmentId)
      .then((companyId: number) => {
        this.fetchNewCycleListForCompanyId(companyId);
      });

    this.currentDepartmentStructureService.setCurrentDepartmentStructureByDepartmentId(departmentId);
    this.currentNavigationService.refreshDepartmentNavigationInformation();

    // this.currentDepartmentId = departmentId;
  }

  fetchNewCycleListForCompanyId(companyId: number): void {
    this.cycleMapperService
      .getCyclesOfCompany(companyId)
      .pipe(take(1))
      .subscribe(cycleList => {
        this.currentCycleList$.next(cycleList);
        this.setCurrentCycleFromList(cycleList, companyId);
      });
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
