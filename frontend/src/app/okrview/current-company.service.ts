import { Injectable } from '@angular/core';
import { Observable, ReplaySubject } from 'rxjs';
import { CompanyUnit } from '../shared/model/ui/OrganizationalUnit/company-unit';
import { take } from 'rxjs/operators';
import { CompanyMapper } from '../shared/services/mapper/company.mapper';

@Injectable({
  providedIn: 'root'
})
export class CurrentCompanyService {

  private currentCompany$: ReplaySubject<CompanyUnit> = new ReplaySubject<CompanyUnit>();

  constructor(private companyMapperService: CompanyMapper) { }

  setCurrentCompanyByCompanyId(companyId: number): void {
    this.companyMapperService
      .getCompanyById$(companyId)
      .pipe(take(1))
      .subscribe(company => {
        this.currentCompany$.next(company);
      });
  }

  setCurrentCompanyByChildDepartmentId(departmentId: number): Promise<number> { // TODO: Remove Promise when current cycle service is implemented
    return new Promise<number>(resolve => {
      this.companyMapperService
        .getParentCompanyOfDepartment$(departmentId)
        .pipe(take(1))
        .subscribe(company => {
          this.currentCompany$.next(company);
          resolve(company.id);
          // this.fetchNewCycleListForCompanyId(company.id); // TODO: Other implementation when current cycle service is implemented
        });
    });
  }

  getCurrentCompany$(): Observable<CompanyUnit> {
    return this.currentCompany$.asObservable();
  }
}
