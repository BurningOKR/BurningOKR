import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs/internal/Observable';
import { map, switchMap, take } from 'rxjs/operators';
import { OkrDepartment } from '../../../shared/model/ui/OrganizationalUnit/okr-department';
import { DepartmentMapper } from '../../../shared/services/mapper/department.mapper';
import {
  ChartCreationOptionsDto,
  ChartTypeEnum,
  ChartTypeEnumMapping,
  InformationTypeEnum,
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
  chartTypes = Object.values(ChartTypeEnum).slice(Object.values(ChartTypeEnum).length / 2); // Enum fun. We only need the first half. The last half is useless.
  chartTypeEnumMapping = ChartTypeEnumMapping;

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
      Should this comment be here after the 31-05-2022 then I didnt have enough time to do it and i recommend creating a U.S. for this.
  */

  ngOnInit(): void {
    this.resetNewChart();
    this.teams$ = this.activatedRoute.paramMap
      .pipe(
        map(params => +params.get('companyId')),
        switchMap(companyId => this.departmentService.getAllDepartmentsForCompanyFlatted$(companyId)));
  }

  addChart(): void {
    // This is very ugly but is sufficient for now/testing
    // As the very early version only has Progression in a line-chart and topicdraft-overview in a pie-chart
    // I decided to simply glue them together in this ugly mess. As seen in the comment above, this entire component needs refactoring
    // Information type is needed to select different types of infos you want to see since a line-chart can show more than just progression
    // or a pie-chart more than the current state of topic-drafts
    this.newChart.informationType = this.newChart.chartType.valueOf();
    this.charts.Add(this.newChart);
    console.log(this.newChart);
    this.resetNewChart();
  }

  deleteChart(chartToDelete: ChartCreationOptionsDto): void {
    this.charts = this.charts.filter(chart => chart !== chartToDelete);
  }

  createDashboardAndRouteToDashboard$(): void {
    const dashboard: DashboardCreationDto = {
      title: this.dashboardTitle,
      chartCreationOptions: this.charts,
    };
    dashboard.title = this.dashboardTitle;
    dashboard.chartCreationOptions = this.charts;

    this.dashboardService.createDashboard$(dashboard)
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

  dashboardValid(): boolean {
    return this.dashboardTitle.trim() && !!this.charts.length;
  }

  private resetNewChart(): void {
    this.newChart = {
      title: '',
      chartType: ChartTypeEnum.LINE,
      informationType: InformationTypeEnum.PROGRESS,
      teamIds: [],
    };
  }

  private navigateToCreatedDashboard(dashboardId: number): void {
    this.router.navigateByUrl(`${location.origin}/${dashboardId}`);
  }
}
