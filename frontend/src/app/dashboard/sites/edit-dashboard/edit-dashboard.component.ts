import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';
import { Dashboard } from '../../model/ui/dashboard';
import { map, switchMap } from 'rxjs/operators';
import { ActivatedRoute } from '@angular/router';
import { DashboardService } from '../../services/dashboard.service';

@Component({
  selector: 'app-edit-dashboard',
  templateUrl: './edit-dashboard.component.html',
  styleUrls: ['./edit-dashboard.component.scss'],
})
export class EditDashboardComponent implements OnInit {
  dashboard$: Observable<Dashboard>;

  constructor(private readonly activatedRoute: ActivatedRoute, private readonly dashboardService: DashboardService) {
  }

  ngOnInit(): void {
    this.dashboard$ = this.activatedRoute.paramMap.pipe(
      map(params => +params.get('dashboardId')),
      switchMap((dashboardId: number) => this.dashboardService.getDashboardById$(dashboardId)),
    );
  }

}
