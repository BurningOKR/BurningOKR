import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs/internal/Observable';
import { map, switchMap, take } from 'rxjs/operators';
import { OkrDepartment } from '../../../shared/model/ui/OrganizationalUnit/okr-department';
import { DepartmentMapper } from '../../../shared/services/mapper/department.mapper';
import { ChartCreationOptionsDto, ChartTypeEnum } from '../../model/dto/chart-creation-options.dto';
import { DashboardCreationDto } from '../../model/dto/dashboard-creation.dto';
import { DashboardService } from '../../services/dashboard.service';

@Component({
  selector: 'app-create-dashboard',
  templateUrl: './create-dashboard.component.html',
  styleUrls: ['./create-dashboard.component.scss'],
})
export class CreateDashboardComponent implements OnInit {
  teams$: Observable<OkrDepartment[]>;
  chartTypes = Object.values(ChartTypeEnum);

  dashboardTitle: string = '';
  charts: ChartCreationOptionsDto[] = [];
  newChart: ChartCreationOptionsDto;

  constructor(private readonly departmentService: DepartmentMapper,
              private readonly activatedRoute: ActivatedRoute,
              private readonly router: Router,
              private readonly dashboardService: DashboardService) {
  }

  /* TODO: p.b. 29-03-2022
      I will rework (IF I HAVE TIME) this entire component to use a dynamic generated formgroup depending on the selection the user has made.
      Should this comment be here after the 31-05-2022 then I didnt have enough time to do it and i recommend either creating a U.S. for this.
  */

  ngOnInit(): void {
    this.resetNewChart();
    this.teams$ = this.activatedRoute.paramMap
      .pipe(
        map(params => +params.get('companyId')),
        switchMap(companyId => this.departmentService.getAllDepartmentsForCompanyFlatted$(companyId)));
  }

  addChart(): void {
    this.charts.Add(this.newChart);
    this.resetNewChart();
  }

  deleteChart(chartToDelete: ChartCreationOptionsDto): void {
    this.charts = this.charts.filter(chart => chart !== chartToDelete);
  }

  createDashboardAndRouteToDashboard$(): void {
    const dashboard: DashboardCreationDto = {
      title: this.dashboardTitle,
      charts: this.charts,
    };
    dashboard.title = this.dashboardTitle;
    dashboard.charts = this.charts;

    this.dashboardService.postDashboard$(dashboard)
      .pipe(take(1),
        map(createdDashboard => createdDashboard.dashboardCreationId))
      .subscribe(dashboardId => {
        this.navigateToCreatedDashboard(dashboardId);
      });
  }

  newChartIsLineChart(): boolean {
    return this.newChart.chartType === ChartTypeEnum.LINE;
  }

  newChartValid(): boolean {
    return !!(this.newChart.title && this.newChart.chartType);
  }

  private resetNewChart(): void {
    this.newChart = {
      title: '',
      chartType: ChartTypeEnum.LINE,
      teams: undefined,
    };
  }

  private navigateToCreatedDashboard(dashboardId: number): void {
    this.router.navigateByUrl(`${location.origin}/${dashboardId}`);
  }
}
