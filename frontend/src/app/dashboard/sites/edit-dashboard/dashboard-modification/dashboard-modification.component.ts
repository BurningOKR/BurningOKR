import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Dashboard } from '../../../model/ui/dashboard';
import { ChartCreationOptionsDto, ChartTypeEnumDropDownRecord } from '../../../model/dto/chart-creation-options.dto';
import { BaseChartOptions } from '../../../model/ui/base-chart-options';
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
  @Output() updateDashboard: EventEmitter<Dashboard> = new EventEmitter<Dashboard>();
  @Output() clickedDelete: EventEmitter<ChartCreationOptionsDto> = new EventEmitter<ChartCreationOptionsDto>();
  allTeams$: Observable<OkrDepartment[]>;
  chartTypeRecord = ChartTypeEnumDropDownRecord;

  constructor(private readonly departmentService: DepartmentMapper) {
  }

  ngOnInit(): void {
    this.allTeams$ = this.departmentService.getAllDepartmentsForCompanyFlatted$(this.dashboard.companyId);
  }

  /**
   * Not all Chart Types can have Teams assigned to them.
   * For those who can't selectedTeamIds is null.
   *
   * @param curr_chart
   */
  teamIsSelectable(curr_chart: BaseChartOptions): boolean {
    return !!curr_chart.selectedTeamIds;
  }

  deleteChart(chartToDelete: BaseChartOptions): void {
    this.dashboard.charts.splice(this.dashboard.charts.indexOf(chartToDelete), 1);
  }

}
