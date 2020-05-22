import { CompanyDto } from '../model/api/company.dto';
import { CompanyUnit } from '../model/ui/OrganizationalUnit/company-unit';
import { Observable, of } from 'rxjs';
import { CycleUnit } from '../model/ui/cycle-unit';
import { CycleWithHistoryCompany } from '../model/ui/cycle-with-history-company';

export class CompanyMapperMock {
  static mapCompany(company: CompanyDto): CompanyUnit {
    return new CompanyUnit(
      company.structureId,
      company.structureName,
      company.departmentIds,
      company.objectiveIds,
      company.cycleId,
      company.label,
      company.corporateObjectiveIds
    );
  }
  static mapCompanyUnit(companyUnit: CompanyUnit): CompanyDto {
    return {
      structureId: companyUnit.id,
      structureName: companyUnit.name,
      departmentIds: companyUnit.departmentIds,
      corporateObjectiveIds: companyUnit.corporateObjectiveStructureIds,
      objectiveIds: companyUnit.objectives,
      cycleId: companyUnit.cycleId,
      label: companyUnit.label
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
