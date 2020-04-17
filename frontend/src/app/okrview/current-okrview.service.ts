import { Injectable } from '@angular/core';
import { Subscription } from 'rxjs';
import { CompanyMapper } from '../shared/services/mapper/company.mapper';
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
  // currentCycle$ = new ReplaySubject<CycleUnit>();
  // currentCycleList$ = new ReplaySubject<CycleUnit[]>();

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
    this.fetchNewValuesForCompanyId(companyId);
  }

  browseDepartment(departmentId: number): void {
    this.fetchNewValuesForDepartmentId(departmentId);
  }

  refreshCurrentDepartmentView(departmentId: number): void {
    this.fetchNewValuesForDepartmentId(departmentId);
  }

  refreshCurrentCompanyView(companyId: number): void {
    this.fetchNewValuesForCompanyId(companyId);
  }

  private fetchNewValuesForCompanyId(companyId: number): void {
    this.currentNavigationService.clearDepartmentNavigationInformation();
    this.currentCompanyService.setCurrentCompanyByCompanyId(companyId);

    // this.currentCompanyId = companyId;

    this.currentDepartmentStructureService.setCurrentDepartmentStructureByCompanyId(companyId);
  }

  private fetchNewValuesForDepartmentId(departmentId: number): void {
    this.currentCompanyService.setCurrentCompanyByChildDepartmentId(departmentId);

    this.currentDepartmentStructureService.setCurrentDepartmentStructureByDepartmentId(departmentId);
    this.currentNavigationService.refreshDepartmentNavigationInformation();

    // this.currentDepartmentId = departmentId;
  }
}
