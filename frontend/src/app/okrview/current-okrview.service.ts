import { Injectable } from '@angular/core';
import { CompanyMapper } from '../shared/services/mapper/company.mapper';
import { CycleMapper } from '../shared/services/mapper/cycle.mapper';
import { StructureSchemaMapper } from '../shared/services/mapper/structure-schema-mapper.service';
import { CurrentStructureSchemeService } from './current-structure-scheme.service';
import { CurrentNavigationService } from './current-navigation.service';
import { CurrentCompanyService } from './current-company.service';

@Injectable({
  providedIn: 'root'
})
export class CurrentOkrviewService {

  constructor(
    private structureSchemaMapperService: StructureSchemaMapper,
    private companyMappersService: CompanyMapper,
    private cycleMapperService: CycleMapper,
    private currentstructureSchemaService: CurrentStructureSchemeService,
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
    this.currentstructureSchemaService.setCurrentStructureSchemaByCompanyId(companyId);
  }

  private fetchNewValuesForDepartmentId(departmentId: number): void {
    this.currentCompanyService.setCurrentCompanyByChildDepartmentId(departmentId);
    this.currentstructureSchemaService.setCurrentStructureSchemaByStructureId(departmentId);
    this.currentNavigationService.refreshDepartmentNavigationInformation();

  }
}
