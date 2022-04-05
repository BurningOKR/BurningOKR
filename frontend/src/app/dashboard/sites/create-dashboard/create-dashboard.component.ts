import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs/internal/Observable';
import { map, switchMap, take, tap } from 'rxjs/operators';
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
  chartTypes = ChartTypeEnum;

  dashboardCreationDto: DashboardCreationDto = {} as DashboardCreationDto; // This sets default values to prevent 'property of undefined' error on title
  charts: ChartCreationOptionsDto[] = [];
  newChart: ChartCreationOptionsDto;

  constructor(private readonly departmentService: DepartmentMapper,
              private readonly activatedRoute: ActivatedRoute,
              private readonly router: Router,
              private readonly dashboardService: DashboardService) {
  }

  /* TODO: p.b. 29-03-2022
      I will rework (IF I HAVE TIME) this entire component to use a dynamic generated formgroup depending on the selection the user has made.
      Should this comment be here after the 31-05-2022 then I didnt have enough time to do it and i recommend creating a U.S. for this.
  */
  ngOnInit(): void {
    this.dashboardCreationDto.title = '';
    this.resetNewChart();
    this.teams$ = this.activatedRoute.paramMap
      .pipe(
        map(params => +params.get('companyId')),
        tap(companyId => this.dashboardCreationDto.companyId = companyId),
        switchMap(companyId => this.departmentService.getAllDepartmentsForCompanyFlatted$(companyId)));
  }

  addChart(): void {
    this.charts.Add(this.newChart);
    console.log(this.newChart);
    this.resetNewChart();
  }

  deleteChart(chartToDelete: ChartCreationOptionsDto): void {
    this.charts = this.charts.filter(chart => chart !== chartToDelete);
  }

  createDashboardAndRouteToDashboard$(): void {
    this.dashboardCreationDto.chartCreationOptions = this.charts;

    this.dashboardService.createDashboard$(this.dashboardCreationDto)
      .pipe(take(1),
        map(createdDashboard => createdDashboard.dashboardCreationId))
      .subscribe(dashboardId => {
        this.navigateToCreatedDashboard(dashboardId);
      });
  }

  newChartIsLineChart(): boolean {
    return this.newChart.chartType === ChartTypeEnum.LINE_PROGRESS;
  }

  newChartValid(): boolean {
    return !!(this.newChart.title && this.newChart.chartType);
  }

  dashboardValid(): boolean {
    return this.dashboardCreationDto.title.trim() && !!this.charts.length;
  }

  private resetNewChart(): void {
    this.newChart = {
      title: '',
      chartType: ChartTypeEnum.LINE_PROGRESS,
      teamIds: [],
    };
  }

  private navigateToCreatedDashboard(dashboardId: number): void {
    this.router.navigateByUrl(`${location.origin}/${dashboardId}`);
  }
}
