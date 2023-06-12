import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { ChartInformationTypeEnum, ChartTypeEnumDropDownRecord } from '../../../model/dto/chart-creation-options.dto';
import { MatSelectChange } from '@angular/material/select';

@Component({
  selector: 'app-pick-chart-type-modal',
  templateUrl: './pick-chart-type-modal.component.html',
  styleUrls: ['./pick-chart-type-modal.component.scss'],
})
export class PickChartTypeModalComponent {

  selectedType: ChartInformationTypeEnum;
  chartTypeRecord = ChartTypeEnumDropDownRecord;
  chartTypes = Object.keys(ChartInformationTypeEnum).slice(0, Object.keys(ChartInformationTypeEnum).length / 2);

  constructor(public dialogRef: MatDialogRef<PickChartTypeModalComponent>) {
  }

  chartSelected(change: MatSelectChange): void {
    this.selectedType = change.value;
  }

  onNoClick(): void {
    this.dialogRef.close();
  }
}
