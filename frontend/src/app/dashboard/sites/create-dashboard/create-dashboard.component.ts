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
  charts: ChartCreationOptionsDto[] = [];
  newChart: ChartCreationOptionsDto;

  teams$: Observable<OkrDepartment[]>;

  constructor(private readonly companyService: CompanyMapper,
              private readonly departmentService: DepartmentMapper,
              private readonly activatedRoute: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.resetNewChart();
    this.teams$ = this.activatedRoute.paramMap
      .pipe(
        map(params => +params.get('companyId')),
        switchMap(companyId => this.departmentService.getAllDepartmentsForCompanyFlatted$(companyId)));
  }

  addDashboard(): void {
    this.charts.Add(this.newChart);
  }

  resetNewChart(): void {
    this.newChart = {
      title: '',
      chartType: ChartTypeEnum.line,
      okrUnitId: undefined,
    };
  }
}
