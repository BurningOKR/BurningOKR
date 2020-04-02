import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, ReplaySubject, Subscription } from 'rxjs';
import { CompanyMapper } from '../shared/services/mapper/company.mapper';
import { DepartmentStructureDto } from '../shared/model/api/department-structure.dto';
import { CycleUnit } from '../shared/model/ui/cycle-unit';
import { CycleWithHistoryCompany } from '../shared/model/ui/cycle-with-history-company';
import { DepartmentStructure, DepartmentStructureRole } from '../shared/model/ui/department-structure';
import { take } from 'rxjs/operators';
import { CycleMapper } from '../shared/services/mapper/cycle.mapper';
import { CompanyUnit } from '../shared/model/ui/OrganizationalUnit/company-unit';
import { DepartmentStructureMapper } from '../shared/services/mapper/department-structure.mapper';

export class DepartmentNavigationInformation {
  departmentId: number;
  departmentsToOpen: number[];

  constructor(id: number, departmentsToOpen: number[]) {
    this.departmentId = id;
    this.departmentsToOpen = departmentsToOpen;
  }
}

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
  cycleWithHistoryCompany: CycleWithHistoryCompany;

  currentCompany$ = new ReplaySubject<CompanyUnit>();
  currentCycle$ = new ReplaySubject<CycleUnit>();
  currentCycleList$ = new ReplaySubject<CycleUnit[]>();
  currentDepartmentStructure$ = new ReplaySubject<DepartmentStructure[]>();
  currentDepartmentNavigation$ = new BehaviorSubject<DepartmentNavigationInformation>(
    new DepartmentNavigationInformation(-1, [])
  );

  constructor(
    private departmentStructureMapperService: DepartmentStructureMapper,
    private companyMappersService: CompanyMapper,
    private cycleMapperService: CycleMapper
  ) {}

  browseCompany(companyId: number): void {
    this.clearNavigationInformation();
    if (this.currentCompanyId === companyId) {
      return;
    }
    this.currentCompanyId = companyId;
    this.currentDepartmentId = undefined;
    this.clearCurrentHard();
    this.fetchNewValuesForCompanyId(companyId);
  }

  browseDepartment(departmentId: number): void {
    if (this.currentDepartmentId === departmentId) {
      return;
    }
    this.currentDepartmentId = departmentId;
    if (!this.isDepartmentInStructure(departmentId, this.currentDepartmentStructure)) {
      this.clearCurrentHard();
      this.fetchNewValuesForDepartmentId(departmentId);
    } else {
      this.refreshNavigationInformation();
    }
  }

  refreshNavigationInformation(): void {
    const idListToOpen: number[] = this.getDepartmentIdListToReachDepartmentWithId(this.currentDepartmentId);
    this.currentDepartmentNavigation$.next(new DepartmentNavigationInformation(this.currentDepartmentId, idListToOpen));
  }

  clearNavigationInformation(): void {
    this.currentDepartmentNavigation$.next(new DepartmentNavigationInformation(-1, []));
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

  clearCurrentHard(): void {
    this.clearSubscriptions();
    this.currentCompany$.next(undefined);
    this.currentDepartmentStructure$.next(undefined);
  }

  refreshCurrentDepartmentView(departmentId: number): void {
    this.clearSubscriptions();
    this.fetchNewValuesForDepartmentId(departmentId);
  }

  refreshCurrentCompanyView(companyId: number): void {
    this.clearSubscriptions();
    this.fetchNewValuesForCompanyId(companyId);
  }

  getCurrentDepartmentStructureList$(): Observable<DepartmentStructure[]> {
    return this.currentDepartmentStructure$;
  }

  getCurrentCompany$(): Observable<CompanyUnit> {
    return this.currentCompany$;
  }

  getCurrentNavigationInformation$(): Observable<DepartmentNavigationInformation> {
    return this.currentDepartmentNavigation$;
  }

  getCurrentCycleList$(): Observable<CycleUnit[]> {
    return this.currentCycleList$;
  }

  getCurrentCycle$(): Observable<CycleUnit> {
    return this.currentCycle$;
  }

  getDepartmentStructureListToReachDepartmentWithId(departmentId: number): DepartmentStructure[] {
    const departmentStructureList: DepartmentStructure[] = [];
    this.getDepartmentStructureListToReachDepartmentWithIdRecursive(
      departmentId,
      this.currentDepartmentStructure,
      departmentStructureList
    );

    return departmentStructureList;
  }

  private getDepartmentStructureListToReachDepartmentWithIdRecursive(
    departmentId: number,
    structure: DepartmentStructure[],
    structureListToOpen: DepartmentStructure[]
  ): void {
    if (structure) {
      for (const subDepartment of structure) {
        if (this.isDepartmentInStructure(departmentId, subDepartment.subDepartments)) {
          structureListToOpen.push(subDepartment);
          this.getDepartmentStructureListToReachDepartmentWithIdRecursive(
            departmentId,
            subDepartment.subDepartments,
            structureListToOpen
          );
        }
      }
    }
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

  private getDepartmentIdListToReachDepartmentWithId(departmentId: number): number[] {
    const departmentStructureList: DepartmentStructure[] = [];
    this.getDepartmentStructureListToReachDepartmentWithIdRecursive(
      departmentId,
      this.currentDepartmentStructure,
      departmentStructureList
    );
    let departmentIdList: number[] = [];
    if (departmentStructureList) {
      departmentIdList = departmentStructureList.map(structure => structure.id);
    }

    return departmentIdList;
  }
// TODO: refactor, so that there are at minimum 2 returns
  private isDepartmentInStructure(departmentId: number, structure: DepartmentStructureDto[]): boolean {
    if (structure) {
      for (const subStructure of structure) {
        if (subStructure.id === departmentId) {
          return true;
        } else if (this.isDepartmentInStructure(departmentId, subStructure.subDepartments)) {
          return true;
        }
      }

      return false;
    }
  }

  private fetchNewValuesForCompanyId(companyId: number): void {
    this.clearNavigationInformation();
    this.companySubscription = this.companyMappersService
      .getCompanyById$(companyId)
      .pipe(take(1))
      .subscribe(company => {
        this.currentCompany = company;
        this.currentCompany$.next(company);
      });
    this.departmentStructureSubscription = this.departmentStructureMapperService
      .getDepartmentStructureOfCompany$(companyId)
      .pipe(take(1))
      .subscribe(departmentStructure => {
        this.currentDepartmentStructure = departmentStructure;
        this.currentDepartmentStructure$.next(departmentStructure);
      });

    this.currentCompanyId = companyId;
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
    this.departmentStructureSubscription = this.departmentStructureMapperService
      .getDepartmentStructureOfDepartment$(departmentId)
      .pipe(take(1))
      .subscribe(departmentStructure => {
        this.currentDepartmentStructure = departmentStructure;
        this.currentDepartmentStructure$.next(departmentStructure);
        this.refreshNavigationInformation();
      });

    this.currentDepartmentId = departmentId;
  }

  private fetchNewCycleListForCompanyId(companyId: number): void {
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
