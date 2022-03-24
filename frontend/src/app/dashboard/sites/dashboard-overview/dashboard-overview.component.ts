import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
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
  currentCompanyId: number;

  constructor(private readonly activatedRoute: ActivatedRoute,
              private readonly dashboardService: DashboardService) {
  }

  ngOnInit(): void {
    this.currentCompanyDashboards$ = this.activatedRoute.paramMap.pipe(
      map(params => +params.get('companyId')),
      switchMap((companyId: number) => {
        this.currentCompanyId = companyId;

        return this.dashboardService.getDashboardsByCompanyId$(companyId);
      }),
      );
  }
}
