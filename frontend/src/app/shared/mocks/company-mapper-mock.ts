import { CompanyDto } from '../model/api/OkrUnit/company.dto';
import { CompanyUnit } from '../model/ui/OrganizationalUnit/company-unit';
import { Observable, of } from 'rxjs';
import { CycleUnit } from '../model/ui/cycle-unit';
import { CycleWithHistoryCompany } from '../model/ui/cycle-with-history-company';

export class CompanyMapperMock {
  static mapCompany(company: CompanyDto): CompanyUnit {
    return {
      id: company.okrUnitId,
      name: company.unitName,
      okrChildUnitIds: company.okrChildUnitIds,
      objectives: company.objectiveIds,
      cycleId: company.cycleId,
      label: company.label,
    };
  }

  static mapCompanyUnit(companyUnit: CompanyUnit): CompanyDto {
    return {
      okrUnitId: companyUnit.id,
      unitName: companyUnit.name,
      okrChildUnitIds: companyUnit.okrChildUnitIds,
      objectiveIds: companyUnit.objectives,
      cycleId: companyUnit.cycleId,
      label: companyUnit.label,
    };
  }

  putCompany$(company: CompanyUnit): Observable<CompanyUnit> {
    return of();
  }

  postCompany$(company: CompanyDto): Observable<CompanyUnit> {
    return of();
  }

  getActiveCompanies$(): Observable<CompanyUnit[]> {
    return of();
  }

  getCompanyHistoryByCompanyId$(companyId: number): Observable<CompanyUnit[]> {
    return of();
  }

  getCyclesOfCompanyHistory$(companyId: number): Observable<CycleUnit[]> {
    return of();
  }

  getCyclesWithHistoryCompanies$(companyId: number): Observable<CycleWithHistoryCompany[]> {
    return of();
  }

  getParentCompanyOfDepartment$(departmentId: number): Observable<CompanyUnit> {
    return of();
  }

  getCompanyById$(companyId: number): Observable<CompanyUnit> {
    return of();
  }

  deleteCompany$(companyId: number): Observable<boolean> {
    return of();
  }
}
