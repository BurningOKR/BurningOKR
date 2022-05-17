import { Component, OnInit } from '@angular/core';
import { MatSelectChange } from '@angular/material/select';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs/internal/Observable';
import { map, switchMap, take } from 'rxjs/operators';
import { OkrDepartment } from '../../../shared/model/ui/OrganizationalUnit/okr-department';
import { DepartmentMapper } from '../../../shared/services/mapper/department.mapper';
import {
  ChartCreationOptionsDto,
  ChartInformationTypeEnum,
  ChartTypeEnumDropDownRecord,
} from '../../model/dto/chart-creation-options.dto';
import { DashboardCreationDto } from '../../model/dto/dashboard-creation.dto';
import { DashboardService } from '../../services/dashboard.service';

@Component({
  selector: 'app-create-dashboard',
  templateUrl: './create-dashboard.component.html',
  styleUrls: ['./create-dashboard.component.scss'],
})
export class CreateDashboardComponent implements OnInit {
  teams$: Observable<OkrDepartment[]>;
  companyId$: Observable<number>;
  chartTypes = Object.keys(ChartInformationTypeEnum).slice(0, Object.keys(ChartInformationTypeEnum).length / 2);
  chartTypeRecord = ChartTypeEnumDropDownRecord;

  dashboardCreationDto: DashboardCreationDto = {} as DashboardCreationDto; // This sets default values to prevent
                                                                           // 'property of undefined' error on title
  charts: ChartCreationOptionsDto[] = [];
  newChart: ChartCreationOptionsDto;

  teamIsSelectable = false;

  constructor(private readonly departmentService: DepartmentMapper,
              private readonly activatedRoute: ActivatedRoute,
              private readonly router: Router,
              private readonly dashboardService: DashboardService) {
  }

  ngOnInit(): void {
    this.dashboardCreationDto.title = '';
    this.companyId$ = this.activatedRoute.paramMap.pipe(
      map(params => {
        this.dashboardCreationDto.companyId = +params.get('companyId');

        return this.dashboardCreationDto.companyId;
      })
    );
    this.resetNewChart();
    this.teams$ = this.companyId$.pipe(
      switchMap(companyId => this.departmentService.getAllDepartmentsForCompanyFlatted$(companyId)),
    );
  }

  addChart(): void {
    this.charts.Add(this.newChart);
    this.resetNewChart();
  }

  deleteChart(chartToDelete: ChartCreationOptionsDto): void {
    this.charts = this.charts.filter(chart => chart !== chartToDelete);
  }

  createDashboardAndRouteToDashboard$(): void {
    this.dashboardCreationDto.chartCreationOptions = this.charts;
    this.dashboardService.createDashboard$(this.dashboardCreationDto)
      .pipe(take(1),
        map(createdDashboard =>
          createdDashboard.id,
        ))
      .subscribe(dashboardId => {
        this.navigateToCreatedDashboard(dashboardId);
      });
  }

  newChartIsLineChart(): boolean {
    return this.newChart.chartType === ChartInformationTypeEnum.LINE_PROGRESS;
  }

  newChartValid(): boolean {
    return !!(this.newChart.title && this.newChart.chartType);
  }

  dashboardValid(): boolean {
    return this.dashboardCreationDto.title.trim() && !!this.charts.length;
  }

  chartSelected(change: MatSelectChange): void {
    this.newChart.chartType = change.value;
    this.teamIsSelectable = this.newChart.chartType.toString() === ChartInformationTypeEnum.LINE_PROGRESS.toString(); // Always  returns  false without toString()
  }

  getTeamNameById(teams: OkrDepartment[], teamId: number): string {
    return teams.find(team => team.id === teamId).name;
  }

  private resetNewChart(): void {
    this.newChart = {
      title: '',
      chartType: ChartInformationTypeEnum.LINE_PROGRESS,
      teamIds: [],
    };
  }

  private navigateToCreatedDashboard(dashboardId: number): void {
    this.router.navigate([`/dashboard/${dashboardId}`]);
  }
}
