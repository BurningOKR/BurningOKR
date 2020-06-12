import { Injectable } from '@angular/core';
import { CompanyMapper } from '../shared/services/mapper/company.mapper';
import { CycleMapper } from '../shared/services/mapper/cycle.mapper';
import { OkrUnitSchemaMapper } from '../shared/services/mapper/okr-unit-schema.mapper';
import { CurrentOkrUnitSchemaService } from './current-okr-unit-schema.service';
import { CurrentNavigationService } from './current-navigation.service';
import { CurrentCompanyService } from './current-company.service';
import { CompanyId, DepartmentId } from '../shared/model/id-types';

@Injectable({
  providedIn: 'root'
})
export class CurrentOkrviewService {

  constructor(
    private okrUnitSchemaMapper: OkrUnitSchemaMapper,
    private companyMappersService: CompanyMapper,
    private cycleMapperService: CycleMapper,
    private currentOkrUnitSchemaService: CurrentOkrUnitSchemaService,
    private currentNavigationService: CurrentNavigationService,
    private currentCompanyService: CurrentCompanyService
  ) {}

  browseCompany(companyId: CompanyId): void {
    this.currentNavigationService.clearDepartmentNavigationInformation();
    this.fetchNewValuesForCompanyId(companyId);
  }

  browseDepartment(departmentId: DepartmentId): void {
    this.currentNavigationService.clearDepartmentNavigationInformation();
    this.fetchNewValuesForDepartmentId(departmentId);
  }

  refreshCurrentDepartmentView(departmentId: CompanyId): void {
    this.fetchNewValuesForDepartmentId(departmentId);
  }

  refreshCurrentCompanyView(companyId: CompanyId): void {
    this.fetchNewValuesForCompanyId(companyId);
  }

  private fetchNewValuesForCompanyId(companyId: CompanyId): void {
    this.currentNavigationService.clearDepartmentNavigationInformation();
    this.currentCompanyService.setCurrentCompanyByCompanyId(companyId);
    this.currentOkrUnitSchemaService.setCurrentUnitSchemaByCompanyId(companyId);
  }

  private fetchNewValuesForDepartmentId(departmentId: DepartmentId): void {
    this.currentCompanyService.setCurrentCompanyByChildDepartmentId(departmentId);
    this.currentOkrUnitSchemaService.setCurrentUnitSchemaByDepartmentId(departmentId);
    this.currentNavigationService.refreshDepartmentNavigationInformation();

  }
}
