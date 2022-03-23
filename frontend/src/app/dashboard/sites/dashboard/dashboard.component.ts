import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/internal/Observable';
import { map, switchMap } from 'rxjs/operators';
import { Dashboard } from '../../model/ui/dashboard';
import { DashboardService } from '../../services/dashboard.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
})
export class DashboardComponent implements OnInit {
  dashboard$: Observable<Dashboard>;

  constructor(private readonly activatedRoute: ActivatedRoute,
              private readonly dashboardService: DashboardService) {
  }

  ngOnInit(): void {
    this.dashboard$ = this.activatedRoute.paramMap.pipe(
      map(params => +params.get('dashboardId')),
      switchMap((dashboardId: number) => this.dashboardService.getDashboardById$(dashboardId)));
  }
}
