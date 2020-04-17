import { Injectable } from '@angular/core';
import { Observable, ReplaySubject, Subscription } from 'rxjs';
import { CompanyMapper } from '../shared/services/mapper/company.mapper';
import { CycleUnit } from '../shared/model/ui/cycle-unit';
import { DepartmentStructure, DepartmentStructureRole } from '../shared/model/ui/department-structure';
import { take } from 'rxjs/operators';
import { CycleMapper } from '../shared/services/mapper/cycle.mapper';
import { CompanyUnit } from '../shared/model/ui/OrganizationalUnit/company-unit';
import { DepartmentStructureMapper } from '../shared/services/mapper/department-structure.mapper';
import { CurrentDepartmentStructureService } from './current-department-structure.service';
import { CurrentNavigationService } from './current-navigation.service';

@Injectable({
  providedIn: 'root'
})
export class CurrentOkrviewService {
  companySubscription: Subscription;
  departmentStructureSubscription: Subscription;
  cycleListSubscription: Subscription;

  currentCompanyId: number;
  currentDepartmentId: number;

  currentCompany: CompanyUnit;
  currentDepartmentStructure: DepartmentStructure[];
  currentCompany$ = new ReplaySubject<CompanyUnit>();
  currentCycle$ = new ReplaySubject<CycleUnit>();
  currentCycleList$ = new ReplaySubject<CycleUnit[]>();
  currentDepartmentStructure$ = new ReplaySubject<DepartmentStructure[]>();

  constructor(
    private departmentStructureMapperService: DepartmentStructureMapper,
    private companyMappersService: CompanyMapper,
    private cycleMapperService: CycleMapper,
    private currentDepartmentStructureService: CurrentDepartmentStructureService,
    private currentNavigationService: CurrentNavigationService
  ) {}

  browseCompany(companyId: number): void {
    this.currentNavigationService.clearDepartmentNavigationInformation();
    if (this.currentCompanyId === companyId) {
      return;
    }
    this.currentCompanyId = companyId;
    this.currentDepartmentId = undefined;
    this.fetchNewValuesForCompanyId(companyId);
  }

  browseDepartment(departmentId: number): void {
    if (this.currentDepartmentId === departmentId) {
      return;
    }
    this.currentDepartmentId = departmentId;
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

  getCurrentCompany$(): Observable<CompanyUnit> {
    return this.currentCompany$;
  }

  getCurrentCycleList$(): Observable<CycleUnit[]> {
    return this.currentCycleList$;
  }

  getCurrentCycle$(): Observable<CycleUnit> {
    return this.currentCycle$;
  }

  updateDepartmentStructureTeamRole(departmentId: number, newRole: DepartmentStructureRole): void {
    this.updateDepartmentStructureTeamRoleRecursive(departmentId, newRole, this.currentDepartmentStructure);
  }

  updateDepartmentStructureTeamRoleRecursive(
    departmentId: number,
    newRole: DepartmentStructureRole,
    departmentStructureList: DepartmentStructure[]
  ): void {
    departmentStructureList.forEach(structure => {
      if (structure.id === departmentId) {
        structure.userRole = newRole;
      } else {
        this.updateDepartmentStructureTeamRoleRecursive(departmentId, newRole, structure.subDepartments);
      }
    });
  }

  private fetchNewValuesForCompanyId(companyId: number): void {
    this.currentNavigationService.clearDepartmentNavigationInformation();
    this.companySubscription = this.companyMappersService
      .getCompanyById$(companyId)
      .pipe(take(1))
      .subscribe(company => {
        this.currentCompany = company;
        this.currentCompany$.next(company);
      });

    this.currentCompanyId = companyId;

    this.currentDepartmentStructureService.setCurrentDepartmentStructureByCompanyId(companyId);
    this.fetchNewCycleListForCompanyId(companyId);
  }

  private fetchNewValuesForDepartmentId(departmentId: number): void {
    this.companySubscription = this.companyMappersService
      .getParentCompanyOfDepartment$(departmentId)
      .pipe(take(1))
      .subscribe(company => {
        this.currentCompany = company;
        this.currentCompanyId = company.id;
        this.currentCompany$.next(company);
        this.fetchNewCycleListForCompanyId(company.id);
      });

    this.currentDepartmentStructureService.setCurrentDepartmentStructureByDepartmentId(departmentId);
    this.currentNavigationService.refreshDepartmentNavigationInformation();

    this.currentDepartmentId = departmentId;
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
