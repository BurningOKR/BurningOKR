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
  // chartFormArray: FormArray;
  teamFormArray: FormArray;

  // tempIdCounter: number = 0;

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
    return <FormArray>this.dbFormGroup.get('formArrayChartTitles');
  }

  get chartControls() {
    return (<FormArray>this.dbFormGroup.get('formArrayChartTitles')).controls;
  }

  get teamControls() {
    return (<FormArray>this.dbFormGroup.get('formArrayTeams')).controls;
  }

  ngOnInit(): void {
    // this.chartFormArray = new FormArray([]);
    this.teamFormArray = new FormArray([]);

    this.dbFormGroup = this.formBuilder.group({
      fcDashboardTitle: new FormControl(this.dashboard.title, [Validators.required]),
      formArrayChartTitles: this.formBuilder.array([this.createChartData()]),
      formArrayTeams: this.teamFormArray,
      // formArrayChartTitles: new FormArray(this.dashboard.charts.map(chart => {
      //   return new FormGroup({
      //     fcChartTitle: new FormControl(chart.title.text),
      //   });
      // })),
    });
    console.log(`Title: ${this.dashboard.title}`);
    // console.log(`Company ID: ${  this.dashboard.companyId}`);
    this.allTeams$ = this.departmentService.getAllDepartmentsForCompanyFlatted$(this.dashboard.companyId);

    for (let chart of this.dashboard.charts) {
      this.onAddChart();
    }
    // this.allTeams$.forEach(x => this.onAddTeam());
  }

  createChartData(): FormGroup {
    return this.formBuilder.group({
      title: [null, [Validators.required]],
      selectedTeamIds: [null, [Validators.required]],
    });
  }

  submitDashboard(): void {
    this.dashboard.title = this.dbFormGroup.get('fcDashboardTitle').value;
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

  onAddChart() {
    this.charts.push(this.createChartData());
    // const control: FormControl = new FormControl(null, [Validators.required]);
    // (<FormArray>this.dbFormGroup.get('formArrayChartTitles')).push(control);
  }

  onAddTeam() {
    const control: FormControl = new FormControl(null);
    (<FormArray>this.dbFormGroup.get('formArrayTeams')).push(control);
  }

  // chartTitleFormControl(chart: BaseChartOptions): string {
  //   const formControl: FormControl = new FormControl();
  //   formControl.setValue(chart.title.text);
  //   const formControlName: string = `Title: ${Math.round(Math.random() * 10000)}`; //this.this.tempIdCounter++
  //   this.dbFormGroup.addControl(formControlName, formControl);
  //
  //   return formControlName;
  // }
  //
  // teamsFormControl(chart: BaseChartOptions): string {
  //   const formControl: FormControl = new FormControl();
  //   formControl.setValue(chart.selectedTeamIds);
  //   const formControlName: string = `Teams: ${Math.round(Math.random() * 10000)}`; //this.this.tempIdCounter++
  //   this.dbFormGroup.addControl(formControlName, formControl);
  //
  //   return formControlName;
  // }

  dashboardValid(): boolean {
    return !!this.dashboard.title.trim(); // this.chartsValid() && this.dashboard.title.trim() && !!this.dashboard.charts.length;
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

