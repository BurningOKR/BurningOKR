import { Component, EventEmitter, HostListener, Input, OnInit, Output } from '@angular/core';
import { Dashboard } from '../../../model/ui/dashboard';
import { ChartInformationTypeEnum, ChartTypeEnumDropDownRecord } from '../../../model/dto/chart-creation-options.dto';
import { BaseChartOptions } from '../../../model/ui/base-chart-options';
import { Observable } from 'rxjs';
import { OkrDepartment } from '../../../../shared/model/ui/OrganizationalUnit/okr-department';
import { DepartmentMapper } from '../../../../shared/services/mapper/department.mapper';
import { LineChartOptions } from '../../../model/ui/line-chart-options';
import { PieChartOptions } from '../../../model/ui/pie-chart-options';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { PickChartTypeModalComponent } from './pick-chart-type-modal/pick-chart-type-modal.component';
import { ComponentCanDeactivate } from '../../../../core/auth/guards/can-deactivate.guard';
import { FormControl, FormGroup } from '@angular/forms';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-dashboard-modification',
  templateUrl: './dashboard-modification.component.html',
  styleUrls: ['./dashboard-modification.component.scss'],
})
export class DashboardModificationComponent implements OnInit, ComponentCanDeactivate {
  @Input() dashboard: Dashboard;
  @Output() updateDashboard: EventEmitter<Dashboard> = new EventEmitter<Dashboard>();
  // @Output() clickedDelete: EventEmitter<ChartCreationOptionsDto> = new EventEmitter<ChartCreationOptionsDto>();
  allTeams$: Observable<OkrDepartment[]>;
  chartTypeRecord = ChartTypeEnumDropDownRecord;
  dbFormGroup: FormGroup;
  tempIdCounter: number = 0;

  constructor(
    private readonly departmentService: DepartmentMapper,
    public dialog: MatDialog,
    private translate: TranslateService,
  ) {
  }

  @HostListener('window:beforeunload', ['$event'])
  unloadHandler(): boolean {
    return this.canDeactivate();
  }

  ngOnInit(): void {
    this.dbFormGroup = new FormGroup({
      fcDashboardTitle: new FormControl(),
    });
    this.allTeams$ = this.departmentService.getAllDepartmentsForCompanyFlatted$(this.dashboard.companyId);
  }

  canDeactivate(): boolean {
    if (this.dbFormGroup.dirty) {
      return confirm(this.translate.instant('edit-dashboard.info.discard-changes'));
    }

    return true;
  }

  chartTitleFormControl(chart: BaseChartOptions): string {
    const formControl: FormControl = new FormControl();
    formControl.setValue(chart.title.text);
    const formControlName: string = `Title: ${this.tempIdCounter++}`;
    this.dbFormGroup.addControl(formControlName, formControl);

    return formControlName;
  }

  teamsFormControl(chart: BaseChartOptions): string {
    const formControl: FormControl = new FormControl();
    formControl.setValue(chart.selectedTeamIds);
    const formControlName: string = `Teams: ${this.tempIdCounter++}`;
    this.dbFormGroup.addControl(formControlName, formControl);

    return formControlName;
  }

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
}

