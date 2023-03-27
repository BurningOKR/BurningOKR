import { Component, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
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
import { ComponentCanDeactivate } from '../../../../core/auth/guards/can-deactivate.guard';
import { NgForm } from '@angular/forms';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-dashboard-modification',
  templateUrl: './dashboard-modification.component.html',
  styleUrls: ['./dashboard-modification.component.scss'],
})
export class DashboardModificationComponent implements OnInit, ComponentCanDeactivate {
  @Input() dashboard: Dashboard;
  @Output() updateDashboard: EventEmitter<Dashboard> = new EventEmitter<Dashboard>();
  @Output() clickedDelete: EventEmitter<ChartCreationOptionsDto> = new EventEmitter<ChartCreationOptionsDto>();
  @ViewChild('dbForm', { read: NgForm }) dbForm: NgForm;
  allTeams$: Observable<OkrDepartment[]>;
  chartTypeRecord = ChartTypeEnumDropDownRecord;

  constructor(
    private readonly departmentService: DepartmentMapper,
    public dialog: MatDialog,
    private translate: TranslateService,
  ) {
  }

  ngOnInit(): void {
    this.allTeams$ = this.departmentService.getAllDepartmentsForCompanyFlatted$(this.dashboard.companyId);
  }

  /**
   * TODO: Selection of teams not yet tracked for changes.
   */
  canDeactivate(): boolean {
    if (this.checkDirty()) {
      return confirm(this.translate.instant('edit-dashboard.info.discard-changes'));
      // return confirm('Do you want to discard changes???');
    }

    return true;
  }

  checkDirty(): boolean {
    return this.dbForm.dirty;
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
    this.dashboard.charts.push(new LineChartOptions());
  }

  addPieChart(): void {
    this.dashboard.charts.push(new PieChartOptions());
  }

  openDialog(): void {
    const dialogRef: MatDialogRef<PickChartTypeModalComponent, string> = this.dialog.open(PickChartTypeModalComponent, {
      width: '500px',
    });

    dialogRef.afterClosed().subscribe(result => {
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

  // @HostListener('window:beforeunload', ['$event'])
  // unloadHandler(event: Event): boolean {
  //   return false;
  // }

  dashboardValid(): boolean {
    return this.chartsValid() && this.dashboard.title.trim() && !!this.dashboard.charts.length;
  }

  chartsValid(): boolean {
    for (const chart of this.dashboard.charts) {
      if (!(chart.title && chart.title.text.trim())) {
        return false;
      }
    }

    return true;
  }

}

