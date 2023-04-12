import { Component, OnInit, ViewChild } from '@angular/core';
import { Dashboard } from '../../../model/ui/dashboard';
import { DepartmentMapper } from '../../../../shared/services/mapper/department.mapper';
import { ActivatedRoute, Router } from '@angular/router';
import { DashboardService } from '../../../services/dashboard.service';
import { DashboardModificationComponent } from '../../edit-dashboard/dashboard-modification/dashboard-modification.component';
import { ComponentCanDeactivate } from '../../../../core/auth/guards/can-deactivate.guard';
import { map, take } from 'rxjs/operators';

@Component({
  selector: 'app-new-dashboard',
  templateUrl: './new-dashboard.component.html',
  styleUrls: ['./new-dashboard.component.scss'],
})
export class NewDashboardComponent implements OnInit, ComponentCanDeactivate {
  @ViewChild('childRef') dashboardModificationComponent!: DashboardModificationComponent;
  newDashboard: Dashboard;

  constructor(
    private readonly departmentService: DepartmentMapper,
    private readonly activatedRoute: ActivatedRoute,
    private readonly router: Router,
    private readonly dashboardService: DashboardService,
  ) {
  }

  ngOnInit(): void {
    console.log(`Company ID: ${+this.activatedRoute.snapshot.paramMap.get('companyId')}`);
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
    console.log(dashboard);
    dashboard.charts.forEach(chart => console.log(`Title of Chart: ${chart.title.text}`));
    // this.dashboardService.createNewDashboard$(dashboard)
    //   .pipe(take(1))
    //   .subscribe();
    this.dashboardService.createNewDashboard$(dashboard)
      .pipe(
        take(1),
        map(createdDashboard => createdDashboard.id),
      )
      .subscribe(dashboardId => {
        this.navigateToCreatedDashboard(dashboardId);
      });

    this.dashboardModificationComponent.dbFormGroup.markAsPristine();
  }

  private navigateToCreatedDashboard(dashboardId: number): void {
    this.router.navigate([`/dashboard/${dashboardId}`]);
  }

}
