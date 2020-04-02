import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { shareReplay, switchMap } from 'rxjs/operators';
import { CompanyMapper } from '../../shared/services/mapper/company.mapper';
import { CompanyUnit } from '../../shared/model/ui/OrganizationalUnit/company-unit';

@Injectable({
  providedIn: 'root'
})
export class ActiveCompaniesService {
  // TODO: refactor
  private companiesSrc$: BehaviorSubject<null> = new BehaviorSubject<undefined>(undefined);
  private readonly _companiesResult$: Observable<CompanyUnit[]>;

  constructor(private companyMapperService: CompanyMapper) {
    this._companiesResult$ = this.companiesSrc$.pipe(
      switchMap(() =>  this.getActiveCompanies$()),
      shareReplay(1)
    );
  }

  getCompaniesResult$(): Observable<CompanyUnit[]> {
    return this._companiesResult$;
  }

  triggerCompaniesUpdate(): void {
    this.companiesSrc$.next(undefined);
  }

  private getActiveCompanies$(): Observable<CompanyUnit[]> {
    return this.companyMapperService.getActiveCompanies$();
  }
}
