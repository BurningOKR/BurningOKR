// istanbul ignore file
import { Injectable } from '@angular/core';
import { ApiHttpService } from '../../../core/services/api-http.service';
import { Observable } from 'rxjs';
import { OkrUnitSchemaDto } from '../../model/api/OkrUnit/okr-unit-schema-dto';
import { CycleDto } from '../../model/api/cycle.dto';
import { CompanyDto } from '../../model/api/OkrUnit/company.dto';
import { CompanyId } from '../../model/id-types';

@Injectable({
  providedIn: 'root'
})
export class CompanyApiService {
  constructor(private api: ApiHttpService) {}

  getActiveCompanies$(): Observable<CompanyDto[]> {
    return this.api.getData$('companies');
  }

  getCompanyHistoryById$(companyId: CompanyId): Observable<CompanyDto[]> {
    return this.api.getData$(`companies/${companyId}/history`);
  }

  getCyclesOfCompany$(companyId: CompanyId): Observable<CycleDto[]> {
    return this.api.getData$(`companies/${companyId}/cycles`);
  }

  getCompanyById$(companyId: CompanyId): Observable<CompanyDto> {
    return this.api.getData$<CompanyDto>(`companies/${companyId}`);
  }

  getOkrUnitSchemaOfCompany$(companyId: CompanyId): Observable<OkrUnitSchemaDto[]> {
    return this.api.getData$(`companies/${companyId}/unit`);
  }

  deleteCompanyById$(companyId: CompanyId): Observable<boolean> {
    return this.api.deleteData$(`companies/${companyId}`);
  }

  postCompany$(company: CompanyDto): Observable<CompanyDto> {
    return this.api.postData$('companies', company);
  }

  putCompany$(company: CompanyDto): Observable<CompanyDto> {
    return this.api.putData$(`companies/${company.unitId}`, company);
  }
}
