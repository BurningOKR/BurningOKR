import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Consts } from '../../shared/consts';
import { DepartmentId } from '../../shared/model/id-types';

@Injectable({
  providedIn: 'root'
})
export class ExcelFileService {

  private readonly _headers;

  constructor(private http: HttpClient) {
    this._headers = {
      headers: [
        new HttpHeaders({'Content-Disposition': 'attachment; filename=okr.xlsx'}),
        new HttpHeaders({application: 'vnd.openxmlformats-officedocument.spreadsheetml.sheet'})
      ]
    };
  }

  downloadExcelFileForOkrTeam$(departmentId: DepartmentId): Observable<any> {
    return this.http.get(`${Consts.API_URL}export/department/${departmentId}`, {
      headers: this._headers,
      responseType: 'blob'
    });
  }

  downloadExcelFileCompany$(companyId: number): Observable<any> {
    return this.http.get(`${Consts.API_URL}export/company/${companyId}`, {
      headers: this._headers,
      responseType: 'blob'
    });
  }

  downloadExcelEmailFileForOkrTeam$(departmentId: DepartmentId): Observable<any> {
    return this.http.get(`${Consts.API_URL}export/email/department/${departmentId}`, {
      headers: this._headers,
      responseType: 'blob'
    });
  }

  downloadExcelEmailFileForCompany$(companyId: number): Observable<any> {
    return this.http.get(`${Consts.API_URL}export/email/company/${companyId}`, {
      headers: this._headers,
      responseType: 'blob'
    });
  }

}
