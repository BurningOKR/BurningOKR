import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';
import { ApiHttpService } from '../../core/services/api-http.service';
import { DashboardDto } from '../model/dto/dashboard.dto';

@Injectable({
  providedIn: 'root'
})
export class DashboardApiService {

  constructor(private readonly api: ApiHttpService) { }

  getDashboardsByCompanyId$(companyId: number): Observable<DashboardDto[]> {
    return this.api.getData$<DashboardDto[]>(`dashboards/company/${companyId}`);
  }

  getDashboardById$(dashboardId: number): Observable<DashboardDto> {
    return this.api.getData$<DashboardDto>(`dashboards/${dashboardId}`);
  }
}
