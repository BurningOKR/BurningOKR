import { Injectable } from '@angular/core';
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
    this.currentNavigationService.clearDepartmentNavigationInformation();
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
    this.currentDepartmentStructureService.setCurrentDepartmentStructuresByCompanyId(companyId);
  }

  private fetchNewValuesForDepartmentId(departmentId: number): void {
    this.currentCompanyService.setCurrentCompanyByChildDepartmentId(departmentId);
    this.currentDepartmentStructureService.setCurrentDepartmentStructuresByDepartmentId(departmentId);
    this.currentNavigationService.refreshDepartmentNavigationInformation();

  }
}
