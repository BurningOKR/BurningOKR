import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { merge, Subject } from 'rxjs';
import { Observable } from 'rxjs/internal/Observable';
import { map, switchMap } from 'rxjs/operators';
import { Dashboard } from '../../model/ui/dashboard';
import { DashboardService } from '../../services/dashboard.service';

@Component({
  selector: 'app-dashboard-overview',
  templateUrl: './dashboard-overview.component.html',
  styleUrls: ['./dashboard-overview.component.scss'],
})
export class DashboardOverviewComponent implements OnInit {
  currentCompanyDashboards$: Observable<Dashboard[]>;
  companyId$: Observable<number>;
  deleteTrigger$: Subject<Dashboard> = new Subject<Dashboard>();

  constructor(private readonly activatedRoute: ActivatedRoute,
              private readonly dashboardService: DashboardService) {
  }

  ngOnInit(): void {
    this.companyId$ = this.activatedRoute.paramMap.pipe(
      map(params => +params.get('companyId')));

    const loadDashboard$: Observable<Dashboard[]> =  this.companyId$.pipe(
      switchMap(companyId => this.dashboardService.getDashboardsByCompanyId$(companyId)));

    const dashboardReloadOnDelete$: Observable<Dashboard[]> = this.deleteTrigger$.pipe(
      switchMap(dashboard => this.dashboardService.deleteDashboardById$(dashboard.id)),
        switchMap(() => this.companyId$),
        switchMap(companyId => this.dashboardService.getDashboardsByCompanyId$(companyId)));

    this.currentCompanyDashboards$ = merge(loadDashboard$, dashboardReloadOnDelete$);
  }

  deleteDashboard(dashboard: Dashboard) {
    this.deleteTrigger$.next(dashboard);
  }
}
