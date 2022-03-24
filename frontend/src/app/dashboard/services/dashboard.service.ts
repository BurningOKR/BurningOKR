import { Injectable } from '@angular/core';
import { of } from 'rxjs';
import { Observable } from 'rxjs/internal/Observable';
import { take } from 'rxjs/operators';
import { CurrentUserService } from '../../core/services/current-user.service';
import { User } from '../../shared/model/api/user';
import { Dashboard } from '../model/ui/dashboard';
import { ChartOptionsBuilderService } from './chart-options-builder.service';
import { DashboardApiService } from './dashboard-api.service';
import { DashboardMapperService } from './dashboard.mapper.service';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {
  currentUser: User;
  constructor(private readonly dashboardApiService: DashboardApiService,
              private readonly dashboardMapper: DashboardMapperService,
              private readonly chartBuilder: ChartOptionsBuilderService,
              private readonly currentUserService: CurrentUserService)  {
    currentUserService.getCurrentUser$().pipe(take(1))
      .subscribe(user => this.currentUser = user);
  } //TODO p.b. remove currentuser stuff, needed this for testing

  getDashboardById$(dashboardId: number): Observable<Dashboard> {
    // return this.dashboardApiService.getDashboardById$(dashboardId).pipe(map(dashboardDto => this.dashboardMapper.mapDtoToUi(dashboardDto)));
    // Für Testzwecke momentan:
    return of({
      id: 1,
      title: 'Erste Diagramm mit einem super langen Namen zum Testen der Namenslänge',
      creator: this.currentUser,
      charts: this.chartBuilder.buildTestCharts(),
    });
  }

  getDashboardsByCompanyId$(companyId: number): Observable<Dashboard[]> {
    /* return this.dashboardApiService.getDashboardsByCompanyId$(companyId).pipe(
      map(dashboardDto => dashboardDto.map(this.dashboardMapper.mapDtoToUi)));
     ZU TESTZWECKEN AUSKOMMENTIERT!  */
     return of([{
       id: 1,
       title: 'Zweite Diagramm mit einem super langen Namen zum Testen der Namenslänge',
       creator: this.currentUser,
       charts: this.chartBuilder.buildTestCharts(),
     },
       {
         id: 1,
         title: 'Zweite Diagramm mit einem super langen Namen zum Testen der Namenslänge',
         creator: this.currentUser,
         charts: this.chartBuilder.buildTestCharts(),
       },
       {
         id: 1,
         title: 'Zweite Diagramm mit einem super langen Namen zum Testen der Namenslänge',
         creator: this.currentUser,
         charts: this.chartBuilder.buildTestCharts(),
       },
       {
         id: 1,
         title: 'Zweite Diagramm mit einem super langen Namen zum Testen der Namenslänge',
         creator: this.currentUser,
         charts: this.chartBuilder.buildTestCharts(),
       },
       {
         id: 1,
         title: 'Zweite Diagramm mit einem super langen Namen zum Testen der Namenslänge',
         creator: this.currentUser,
         charts: this.chartBuilder.buildTestCharts(),
       },
       {
         id: 1,
         title: 'Zweite Diagramm mit einem super langen Namen zum Testen der Namenslänge',
         creator: this.currentUser,
         charts: this.chartBuilder.buildTestCharts(),
       },
       {
         id: 1,
         title: 'Zweite Diagramm mit einem super langen Namen zum Testen der Namenslänge',
         creator: this.currentUser,
         charts: this.chartBuilder.buildTestCharts(),
       },
       {
         id: 1,
         title: 'Zweite Diagramm mit einem super langen Namen zum Testen der Namenslänge',
         creator: this.currentUser,
         charts: this.chartBuilder.buildTestCharts(),
       },
       {
         id: 1,
         title: 'Zweite Diagramm mit einem super langen Namen zum Testen der Namenslänge',
         creator: this.currentUser,
         charts: this.chartBuilder.buildTestCharts(),
       },
       {
         id: 1,
         title: 'Zweite Diagramm mit einem super langen Namen zum Testen der Namenslänge',
         creator: this.currentUser,
         charts: this.chartBuilder.buildTestCharts(),
       },
     ]);
  }
}
