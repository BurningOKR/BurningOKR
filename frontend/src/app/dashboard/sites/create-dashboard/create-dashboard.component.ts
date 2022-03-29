import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/internal/Observable';
import { map, switchMap } from 'rxjs/operators';
import { OkrDepartment } from '../../../shared/model/ui/OrganizationalUnit/okr-department';
import { CompanyMapper } from '../../../shared/services/mapper/company.mapper';
import { DepartmentMapper } from '../../../shared/services/mapper/department.mapper';
import { ChartCreationOptionsDto, ChartTypeEnum } from '../../model/dto/chart-creation-options.dto';

@Component({
  selector: 'app-create-dashboard',
  templateUrl: './create-dashboard.component.html',
  styleUrls: ['./create-dashboard.component.scss'],
})
export class CreateDashboardComponent implements OnInit {
  teams$: Observable<OkrDepartment[]>;

  chartTypes = Object.values(ChartTypeEnum);
  charts: ChartCreationOptionsDto[] = [];
  newChart: ChartCreationOptionsDto;

  constructor(private readonly companyService: CompanyMapper,
              private readonly departmentService: DepartmentMapper,
              private readonly activatedRoute: ActivatedRoute) {
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

  addDashboard(): void {
    this.charts.Add(this.newChart);
    this.resetNewChart();
  }

  deleteChart(chartToDelete: ChartCreationOptionsDto): void {
    this.charts = this.charts.filter(chart => chart !== chartToDelete);
  }

  createDashboard(): void {
    console.log('Dashboard created:');
    this.charts.forEach(chart => console.log(chart));
  }

  newChartIsLineChart(): boolean {
    return this.newChart.chartType === ChartTypeEnum.line;
  }

  newChartValid(): boolean {
    return !!(this.newChart.title && this.newChart.chartType);
  }

  private resetNewChart(): void {
    this.newChart = {
      title: '',
      chartType: ChartTypeEnum.line,
      teams: undefined,
    };
  }
}
