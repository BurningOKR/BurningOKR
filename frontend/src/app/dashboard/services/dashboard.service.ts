import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';
import { map } from 'rxjs/operators';
import { DashboardCreationDto } from '../model/dto/dashboard-creation.dto';
import { Dashboard } from '../model/ui/dashboard';
import { DashboardApiService } from './dashboard-api.service';
import { DashboardMapperService } from './dashboard.mapper.service';

@Injectable({
  providedIn: 'root',
})
export class DashboardService {

  constructor(private readonly dashboardApiService: DashboardApiService,
              private readonly dashboardMapper: DashboardMapperService) {
  }

  getDashboardById$(dashboardId: number): Observable<Dashboard> {
    return this.dashboardApiService.getDashboardById$(dashboardId).pipe(map(dashboardDto =>
      this.dashboardMapper.mapDtoToUi(dashboardDto)));
  }

  getDashboardsByCompanyId$(companyId: number): Observable<Dashboard[]> {
    return this.dashboardApiService.getDashboardsByCompanyId$(companyId).pipe(
      map(dashboardDto => dashboardDto.map(this.dashboardMapper.mapDtoToUi)));
  }

  createDashboard$(dashboard: DashboardCreationDto): Observable<DashboardCreationDto> {
    return this.dashboardApiService.postDashboard$(dashboard);
  }

  deleteDashboardById$(dashboardId: number): Observable<boolean> {
    return this.dashboardApiService.deleteDashboardById$(dashboardId);
  }
}
