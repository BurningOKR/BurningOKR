import { Injectable } from '@angular/core';
import { forkJoin, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/internal/operators';
import { map } from 'rxjs/operators';
import { CompanyDto } from '../../model/api/structure/company.dto';
import { CycleUnit } from '../../model/ui/cycle-unit';
import { CycleWithHistoryCompany } from '../../model/ui/cycle-with-history-company';
import { CompanyUnit } from '../../model/ui/OrganizationalUnit/company-unit';
import { CompanyApiService } from '../api/company-api.service';
import { CycleMapper } from './cycle.mapper';
import { DepartmentApiService } from '../api/department-api.service';

@Injectable({
  providedIn: 'root'
})
export class CompanyMapper {
  constructor(
    private companyApiService: CompanyApiService,
    private cycleMapperService: CycleMapper,
    private departmentApiService: DepartmentApiService
  ) {}

  static mapCompany(company: CompanyDto): CompanyUnit {
    return new CompanyUnit(
      company.structureId,
      company.structureName,
      company.subStructureIds,
      company.objectiveIds,
      company.cycleId,
      company.label
    );
  }
  static mapCompanyUnit(companyUnit: CompanyUnit): CompanyDto {
    return {
      structureId: companyUnit.id,
      structureName: companyUnit.name,
      subStructureIds: companyUnit.subStructureIds,
      objectiveIds: companyUnit.objectives,
      cycleId: companyUnit.cycleId,
      label: companyUnit.label
    };
  }

  putCompany$(company: CompanyUnit): Observable<CompanyUnit> {
    return this.companyApiService.putCompany$(CompanyMapper.mapCompanyUnit(company))
      .pipe(map(CompanyMapper.mapCompany));
  }

  postCompany$(company: CompanyDto): Observable<CompanyUnit> {
    return this.companyApiService.postCompany$(company)
      .pipe(map(CompanyMapper.mapCompany));
  }

  getActiveCompanies$(): Observable<CompanyUnit[]> {
    return this.companyApiService.getActiveCompanies$()
      .pipe(
      map((companies: CompanyDto[]) => {
        return companies
          .map(CompanyMapper.mapCompany)
          .sort((a: CompanyUnit, b: CompanyUnit) => (a.name < b.name ? -1 : a.name === b.name ? 0 : 1));
      })
    );
  }

  getCompanyHistoryByCompanyId$(companyId: number): Observable<CompanyUnit[]> {
    return this.companyApiService.getCompanyHistoryById$(companyId)
      .pipe(
      map((companies: CompanyDto[]) => {
        return companies
          .map(CompanyMapper.mapCompany)
          .sort((a: CompanyUnit, b: CompanyUnit) => (a.name < b.name ? -1 : a.name === b.name ? 0 : 1));
      })
    );
  }

  getCyclesOfCompanyHistory$(companyId: number): Observable<CycleUnit[]> {
    return this.getCompanyHistoryByCompanyId$(companyId)
      .pipe(
      mergeMap((companyUnits: CompanyUnit[]) => {
        const cycleUnits$: Observable<CycleUnit>[] = companyUnits.map(company => this.cycleMapperService.getCycleById$(company.cycleId));

        return forkJoin(cycleUnits$);
      })
    );
  }

  getCyclesWithHistoryCompanies$(companyId: number): Observable<CycleWithHistoryCompany[]> {
    return this.getCompanyHistoryByCompanyId$(companyId)
      .pipe(
      mergeMap((companies: CompanyUnit[]) => {
        const cyclesWithCompanies$: Observable<CycleWithHistoryCompany>[] = companies.map(company =>
          this.cycleMapperService.getCycleById$(company.cycleId)
            .pipe(map((cycle: CycleUnit) => {
              return new CycleWithHistoryCompany(cycle, company);
            })
          )
        );

        return forkJoin(cyclesWithCompanies$);
      })
    );
  }

  getParentCompanyOfDepartment$(departmentId: number): Observable<CompanyUnit> {
    return this.departmentApiService.getParentCompanyOfDepartment$(departmentId)
      .pipe(map(CompanyMapper.mapCompany));
  }

  getCompanyById$(companyId: number): Observable<CompanyUnit> {
    return this.companyApiService.getCompanyById$(companyId)
      .pipe(map(CompanyMapper.mapCompany));
  }

  deleteCompany$(companyId: number): Observable<boolean> {
    return this.companyApiService.deleteCompanyById$(companyId);
  }
}
