import { Component, OnInit, ViewChild } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';
import { Dashboard } from '../../../model/ui/dashboard';
import { filter, map, switchMap, take } from 'rxjs/operators';
import { ActivatedRoute } from '@angular/router';
import { DashboardService } from '../../../services/dashboard.service';
import { ComponentCanDeactivate } from '../../../../core/auth/guards/can-deactivate.guard';
import { DashboardModificationComponent } from '../dashboard-modification.component';

@Component({
  selector: 'app-edit-dashboard',
  templateUrl: './edit-dashboard.component.html',
  styleUrls: ['./edit-dashboard.component.scss'],
})
export class EditDashboardComponent implements OnInit, ComponentCanDeactivate {
  @ViewChild('childRef') dashboardModificationComponent!: DashboardModificationComponent;
  dashboard$: Observable<Dashboard>;

  constructor(private readonly activatedRoute: ActivatedRoute, private readonly dashboardService: DashboardService) {
  }

  ngOnInit(): void {
    this.dashboard$ = this.activatedRoute.paramMap.pipe(
      map(params => +params.get('dashboardId')),
      switchMap((dashboardId: number) => this.dashboardService.getDashboardById$(dashboardId)),
      filter(dashboard => !!dashboard),
    );
  }

  updateDashboard(dashboard: Dashboard): void {
    this.dashboardService.updateDashboard$(dashboard)
      .pipe(take(1))
      .subscribe();

    this.dashboardModificationComponent.dbFormGroup.markAsPristine();
  }

  canDeactivate(): boolean {
    return this.dashboardModificationComponent.canDeactivate();
  }

}
