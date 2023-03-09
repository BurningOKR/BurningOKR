import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Dashboard } from '../../../model/ui/dashboard';
import { ChartCreationOptionsDto, ChartTypeEnumDropDownRecord } from '../../../model/dto/chart-creation-options.dto';
import { BaseChartOptions } from '../../../model/ui/base-chart-options';
import { MatSelectChange } from '@angular/material/select';
import { Observable } from 'rxjs';
import { OkrDepartment } from '../../../../shared/model/ui/OrganizationalUnit/okr-department';
import { DepartmentMapper } from '../../../../shared/services/mapper/department.mapper';

@Component({
  selector: 'app-dashboard-modification',
  templateUrl: './dashboard-modification.component.html',
  styleUrls: ['./dashboard-modification.component.scss'],
})
export class DashboardModificationComponent implements OnInit {
  @Input() dashboard: Dashboard;
  // @Input() chart: ChartCreationOptionsDto;
  @Input() allTeams$: Observable<OkrDepartment[]>;
  @Output() updateDashboard: EventEmitter<Dashboard> = new EventEmitter<Dashboard>();
  @Output() clickedDelete: EventEmitter<ChartCreationOptionsDto> = new EventEmitter<ChartCreationOptionsDto>();
  chartTypeRecord = ChartTypeEnumDropDownRecord;

  constructor(
    private readonly departmentService: DepartmentMapper,
    // private readonly activatedRoute: ActivatedRoute,
    // private readonly router: Router,
    // private readonly dashboardService: DashboardService,
  ) {
  }

  ngOnInit(): void {
    console.log(this.dashboard.title);
    // this.allTeams$ = this.departmentService.getAllDepartmentsForCompanyFlatted$(this.dashboard.companyId);
  }

  // getTeamsOfChart(allTeams: OkrDepartment[]): OkrDepartment[] {
  //   return allTeams.filter(team => this.chart.teamIds.includes(team.id));

  // }
  chartTypeSelected(change: MatSelectChange): void {
    // this.newChart.chartType = change.value;
    // this.teamIsSelectable = this.newChart.chartType.toString() === ChartInformationTypeEnum.LINE_PROGRESS.toString(); // Always returns false without toString()
  }

  deleteChart(chartToDelete: BaseChartOptions): void {
    this.dashboard.charts.splice(this.dashboard.charts.indexOf(chartToDelete), 1);
    // this.dashboard.charts.forEach((chart, index) => {
    //   if(chart===chartToDelete) {
    //     this.dashboard.charts.splice(index, 1);
    //   }
    // });
    // this.charts = this.charts.filter(chart => chart !== chartToDelete);
  }

}
