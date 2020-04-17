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

  constructor(private companyMapperService: CompanyMapper) {
  }

  setCurrentCompanyByCompanyId(companyId: number): void {
    this.companyMapperService
      .getCompanyById$(companyId)
      .pipe(take(1))
      .subscribe(company => {
        this.currentCompany$.next(company);
      });
  }

  setCurrentCompanyByChildDepartmentId(departmentId: number): void {
    this.companyMapperService
      .getParentCompanyOfDepartment$(departmentId)
      .pipe(take(1))
      .subscribe(company => {
        this.currentCompany$.next(company);
      });
  }

  getCurrentCompany$(): Observable<CompanyUnit> {
    return this.currentCompany$.asObservable();
  }
}
