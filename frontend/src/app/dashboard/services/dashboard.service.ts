import { Injectable } from '@angular/core';
import { of } from 'rxjs';
import { Observable } from 'rxjs/internal/Observable';
import { Dashboard } from '../model/ui/dashboard';
import { ChartOptionsBuilderService } from './chart-options-builder.service';
import { DashboardApiService } from './dashboard-api.service';
import { DashboardMapperService } from './dashboard.mapper.service';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {

  constructor(private readonly dashboardApiService: DashboardApiService,
              private readonly dashboardMapper: DashboardMapperService,
              private readonly chartBuilder: ChartOptionsBuilderService) { }

  getDashboardById$(dashboardId: number): Observable<Dashboard> {
    // return this.dashboardApiService.getDashboardById$(dashboardId).pipe(map(dashboardDto => this.dashboardMapper.mapDtoToUi(dashboardDto)));
    // Für Testzwecke momentan:
    return of({
      id: 1,
      title: 'Erste Diagramm',
      charts: this.chartBuilder.buildTestCharts(),
    });
  }

  getDashboardsByCompanyId$(companyId: number): Observable<Dashboard[]> {
    /* return this.dashboardApiService.getDashboardsByCompanyId$(companyId).pipe(
      map(dashboardDto => dashboardDto.map(this.dashboardMapper.mapDtoToUi)));
     ZU TESTZWECKEN AUSKOMMENTIERT!  */
     return of([{
       id: 1,
       title: 'Erste Diagramm',
       charts: this.chartBuilder.buildTestCharts(),
     }]);
  }
}
