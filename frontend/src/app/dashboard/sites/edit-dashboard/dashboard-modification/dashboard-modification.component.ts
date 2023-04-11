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
import { FormArray, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-dashboard-modification',
  templateUrl: './dashboard-modification.component.html',
  styleUrls: ['./dashboard-modification.component.scss'],
})
export class DashboardModificationComponent implements OnInit, ComponentCanDeactivate {
  @Input() dashboard: Dashboard;
  @Output() updateDashboard: EventEmitter<Dashboard> = new EventEmitter<Dashboard>();
  allTeams$: Observable<OkrDepartment[]>;
  chartTypeRecord = ChartTypeEnumDropDownRecord;
  dbFormGroup: FormGroup;
  formArrayCharts: FormArray;

  constructor(
    private readonly departmentService: DepartmentMapper,
    public dialog: MatDialog,
    private translate: TranslateService,
    private formBuilder: FormBuilder,
  ) {
  }

  @HostListener('window:beforeunload', ['$event'])
  unloadHandler(): boolean {
    return this.canDeactivate();
  }

  get charts(): FormArray {
    return this.dbFormGroup.get('formArrayCharts') as FormArray;
  }

  ngOnInit(): void {
    this.formArrayCharts = new FormArray([]);

    this.dbFormGroup = this.formBuilder.group({
      fcDashboardTitle: new FormControl(this.dashboard.title, [Validators.required]),
      formArrayCharts: this.formArrayCharts,
    });
    console.log(`Title: ${this.dashboard.title}`);
    this.allTeams$ = this.departmentService.getAllDepartmentsForCompanyFlatted$(this.dashboard.companyId);

    for (const chart of this.dashboard.charts) {
      this.onAddChart(chart);
    }
  }

  submitDashboard(): void {
    this.dashboard.title = this.dbFormGroup.get('fcDashboardTitle').value;
    const chartsFormArray: FormArray = this.dbFormGroup.get('formArrayCharts') as FormArray;
    this.dashboard.charts.forEach((chart, index) => {
      const chartControl: FormControl = chartsFormArray.at(index) as FormControl;
      chart.title.text = chartControl.get('title').value;
      chart.selectedTeamIds = chartControl.get('selectedTeamIds').value;
    });
    if (this.dashboardValid()) {
      this.updateDashboard.emit(this.dashboard);
    } else {
      alert(this.translate.instant('edit-dashboard.info.not-valid'));
    }
  }

  canDeactivate(): boolean {
    console.log(`Title: ${this.dashboard.title}`);
    if (this.dbFormGroup.dirty) {
      return confirm(this.translate.instant('edit-dashboard.info.discard-changes'));
    }

    return true;
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

  createChartData(chart: BaseChartOptions): FormGroup {
    return this.formBuilder.group({
      id: [chart.id],
      title: [chart.title.text, [Validators.required]],
      selectedTeamIds: [chart.selectedTeamIds, [Validators.required]],
    });
  }

  onAddChart(chart: BaseChartOptions) {
    this.charts.push(this.createChartData(chart));
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
    const index: number = this.dashboard.charts.indexOf(chartToDelete);
    const chartsFormArray: FormArray = this.dbFormGroup.get('formArrayCharts') as FormArray;
    this.dashboard.charts.splice(index, 1);
    chartsFormArray.removeAt(index);
  }

  addLineChart(): void {
    const newLC: LineChartOptions = new LineChartOptions();
    this.dashboard.charts.push(newLC);
    this.onAddChart(newLC);
  }

  addPieChart(): void {
    const newPC: PieChartOptions = new PieChartOptions();
    this.dashboard.charts.push(newPC);
    this.onAddChart(newPC);
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

