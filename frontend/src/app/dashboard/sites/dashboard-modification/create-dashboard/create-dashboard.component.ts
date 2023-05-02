import { Component, OnInit, ViewChild } from '@angular/core';
import { Dashboard } from '../../../model/ui/dashboard';
import { DepartmentMapper } from '../../../../shared/services/mapper/department.mapper';
import { ActivatedRoute } from '@angular/router';
import { DashboardService } from '../../../services/dashboard.service';
import { DashboardModificationComponent } from '../dashboard-modification.component';
import { ComponentCanDeactivate } from '../../../../core/auth/guards/can-deactivate.guard';
import { map, take } from 'rxjs/operators';

@Component({
  selector: 'app-create-dashboard',
  templateUrl: './create-dashboard.component.html',
  styleUrls: ['./create-dashboard.component.scss'],
})
export class CreateDashboardComponent implements OnInit, ComponentCanDeactivate {
  @ViewChild('childRef') dashboardModificationComponent!: DashboardModificationComponent;
  newDashboard: Dashboard;

  constructor(
    private readonly departmentService: DepartmentMapper,
    private readonly activatedRoute: ActivatedRoute,
    private readonly dashboardService: DashboardService,
  ) {
  }

  ngOnInit(): void {
    this.newDashboard = {
      id: null,
      title: '',
      charts: [],
      companyId: +this.activatedRoute.snapshot.paramMap.get('companyId'),
      creationDate: new Date(),
    };
  }

  canDeactivate(): boolean {
    return this.dashboardModificationComponent.canDeactivate();
  }

  updateDashboard(dashboard: Dashboard): void {
    this.dashboardService.createDashboard$(dashboard)
      .pipe(
        take(1),
        map(createdDashboard => createdDashboard.id),
      )
      .subscribe(dashboardId => {
        this.dashboardModificationComponent.navigateToCreatedDashboard(dashboardId);
      });

    this.dashboardModificationComponent.dbFormGroup.markAsPristine();
  }

}
