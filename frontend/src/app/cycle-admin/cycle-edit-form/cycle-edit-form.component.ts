import { Component, Inject } from '@angular/core';
import { CycleState, CycleUnit } from '../../shared/model/ui/cycle-unit';
import { CycleMapper } from '../../shared/services/mapper/cycle.mapper';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { CompanyMapper } from '../../shared/services/mapper/company.mapper';
import { Observable } from 'rxjs';
import { DateFormValidator } from '../../shared/validators/date-format-validator/date-format-validator-function';
import { DateNotInThePastValidator } from '../../shared/validators/date-not-in-the-past-validator/date-not-in-the-past-validator-function';
import { DateNotInRangeOfAnotherCycleValidator } from '../../shared/validators/date-range-in-range-within-another-dates-validator/date-range-in-range-within-another-dates-validator-function';
import { StartDateBeforeEndDateValidator } from '../../shared/validators/start-date-before-end-date-validator/start-date-before-end-date-validator-function';

export interface CycleEditFormData {
  cycle: CycleUnit;
  allCyclesOfCompany$: Observable<CycleUnit[]>;
}

@Component({
  selector: 'app-cycle-edit-form',
  templateUrl: './cycle-edit-form.component.html',
  styleUrls: ['./cycle-edit-form.component.scss']
})
export class CycleEditFormComponent {
  cycleStateEnum = CycleState;
  cycleForm: FormGroup;

  constructor(private dialogRef: MatDialogRef<CycleEditFormComponent>,
              private cycleMapper: CycleMapper,
              private companyMapper: CompanyMapper,
              @Inject(MAT_DIALOG_DATA) private formData: CycleEditFormData) {
    this.loadCyclesWithHistoryCompanyInForm();
    this.generateCycleForm();
  }

  isCycleInPreparation(): boolean {
    return this.formData.cycle.cycleState === this.cycleStateEnum.PREPARATION;
  }

  isCycleActive(): boolean {
    return this.formData.cycle.cycleState === this.cycleStateEnum.ACTIVE;
  }

  isCycleClosed(): boolean {
    return this.formData.cycle.cycleState === this.cycleStateEnum.CLOSED;
  }

  saveCycle(): void {
    const cycle: CycleUnit = this.formData.cycle;

    cycle.name = this.cycleForm.get('name').value;
    cycle.startDate = this.cycleForm.get('startDate').value;
    cycle.endDate = this.cycleForm.get('endDate').value;
    cycle.isVisible = this.cycleForm.get('isVisible').value;

    this.dialogRef.close(this.cycleMapper.putCycle$(cycle));
  }

  private generateCycleForm(cycles: CycleUnit[] = []): void {
    const cycle: CycleUnit = this.formData.cycle;
    const isStartDateDisabled: boolean = this.isCycleActive() || this.isCycleClosed();
    const isEndDateDisabled: boolean = this.isCycleClosed();
    const isVisibilityOptionDisabled: boolean = this.isCycleActive();
    this.cycleForm = new FormGroup({
      name: new FormControl(cycle.name, [Validators.required,  Validators.maxLength(255)]),
      startDate: new FormControl({value: cycle.startDate, disabled: isStartDateDisabled},
        [DateFormValidator.Validate, DateNotInThePastValidator.Validate]), // ADD CUSTOM DATE VALIDATOR https://stackoverflow.com/questions/51633047/angular-material-date-validator-get-invalid-value
      endDate: new FormControl({value: cycle.endDate, disabled: isEndDateDisabled},
        [DateFormValidator.Validate]),
      isVisible: new FormControl({value: cycle.isVisible, disabled: isVisibilityOptionDisabled})
    }, [StartDateBeforeEndDateValidator.Validate,
        DateNotInRangeOfAnotherCycleValidator.Validate(cycles)]);
  }

  private loadCyclesWithHistoryCompanyInForm(): void {
    this.formData.allCyclesOfCompany$
      .subscribe((cycles: CycleUnit[]) => {
        this.generateCycleForm(this.removeCurrentCycleOutOfList(cycles, this.formData.cycle.id));
      });
  }

  private removeCurrentCycleOutOfList(cycles: CycleUnit[], currentCycleId: number): CycleUnit[] {
    return cycles.filter((cycle: CycleUnit) => {
      return cycle.id !== currentCycleId;
    });
  }
}
