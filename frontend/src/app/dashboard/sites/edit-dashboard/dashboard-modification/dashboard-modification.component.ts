import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Dashboard } from '../../../model/ui/dashboard';
import {
  ChartCreationOptionsDto,
  ChartInformationTypeEnum,
  ChartTypeEnumDropDownRecord,
} from '../../../model/dto/chart-creation-options.dto';
import { BaseChartOptions } from '../../../model/ui/base-chart-options';
import { Observable } from 'rxjs';
import { OkrDepartment } from '../../../../shared/model/ui/OrganizationalUnit/okr-department';
import { DepartmentMapper } from '../../../../shared/services/mapper/department.mapper';
import { LineChartOptions } from '../../../model/ui/line-chart-options';
import { PieChartOptions } from '../../../model/ui/pie-chart-options';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { PickChartTypeModalComponent } from './pick-chart-type-modal/pick-chart-type-modal.component';

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
  newLineChart: BaseChartOptions = new LineChartOptions();
  newPieChart: BaseChartOptions = new PieChartOptions();

  constructor(private readonly departmentService: DepartmentMapper, public dialog: MatDialog) {
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

  addLineChart(): void {
    // this.newLineChart.chartType = 0;
    this.dashboard.charts.push(this.newLineChart);
    this.resetNewChart();
  }

  addPieChart(): void {
    this.newPieChart.title.text = '';
    // this.newPieChart.chartType = 1;
    this.dashboard.charts.push(this.newPieChart);
    this.resetNewChart();
  }

  openDialog(): void {
    const dialogRef: MatDialogRef<PickChartTypeModalComponent, string> = this.dialog.open(PickChartTypeModalComponent, {
      width: '500px',
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log(typeof result);
      switch (result) {
        case ChartInformationTypeEnum.LINE_PROGRESS.toString():
          return this.addLineChart();
        case ChartInformationTypeEnum.PIE_TOPICDRAFTOVERVIEW.toString():
          return this.addPieChart();
        default:
          return null;
      }
    });
  }

  private resetNewChart(): void {
    // this.newChart = {
    //   title = '',
    //   chartType: ChartInformationTypeEnum.LINE_PROGRESS,
    //   teamIds: [],
    // };
  }

}

