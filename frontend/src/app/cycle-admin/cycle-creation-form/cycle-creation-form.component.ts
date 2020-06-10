import { Component, Inject } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { CycleState, CycleUnit } from '../../shared/model/ui/cycle-unit';
import { ControlHelperService } from '../../shared/services/helper/control-helper.service';
import { MatDialogRef } from '@angular/material';
import { CycleMapper } from '../../shared/services/mapper/cycle.mapper';

import { NEVER } from 'rxjs';
import { CycleDto } from '../../shared/model/api/cycle.dto';
import { CompanyMapper } from '../../shared/services/mapper/company.mapper';
import { dateRangeInRangeWithinAnotherDatesValidatorFunction } from '../../shared/validators/date-range-in-range-within-another-dates-validator/date-range-in-range-within-another-dates-validator-function';
import { startDateBeforeEndDateValidatorFunction } from '../../shared/validators/start-date-before-end-date-validator/start-date-before-end-date-validator-function';
import { dateFormatValidatorFunction } from '../../shared/validators/date-format-validator/date-format-validator-function';
import { I18n } from '@ngx-translate/i18n-polyfill';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { CycleDialogData } from '../../shared/model/ui/cycle-dialog-data';
import { dateNotInThePastValidatorFunction } from '../../shared/validators/date-not-in-the-past-validator/date-not-in-the-past-validator-function';

@Component({
  selector: 'app-cycle-creation-form',
  templateUrl: './cycle-creation-form.component.html',
  styleUrls: ['./cycle-creation-form.component.scss']
})
export class CycleCreationFormComponent {

  cycleForm: FormGroup;
  cycles: CycleUnit[];
  title: string;
  saveAndCloseLabel: string;
  getErrorMessage = this.controlHelperService.getErrorMessage;

  constructor(private dialogRef: MatDialogRef<CycleCreationFormComponent>,
              private cycleMapper: CycleMapper,
              private companyService: CompanyMapper,
              private controlHelperService: ControlHelperService,
              private i18n: I18n,
              @Inject(MAT_DIALOG_DATA) public formData: CycleDialogData) {
    this.formData.cycles$
      .subscribe((cycles: CycleUnit[]) => {
        this.cycles = cycles;
        this.cycleForm = new FormGroup({
          name: new FormControl('', [Validators.required, Validators.maxLength(255)]),
          startDate: new FormControl('', [dateFormatValidatorFunction, dateNotInThePastValidatorFunction]),
          endDate: new FormControl('', [dateFormatValidatorFunction]),
          isVisible: new FormControl(true)
        }, [startDateBeforeEndDateValidatorFunction,
            dateRangeInRangeWithinAnotherDatesValidatorFunction(this.cycles)]);
      });

    this.title = this.i18n({
      id: 'createNewCycle',
      value: 'Neuen Zyklus erstellen'
    });
    this.saveAndCloseLabel = this.i18n({
      id: 'defineCycle',
      value: 'Zyklus definieren'
    });
  }

  closeDialog(): void {
    this.dialogRef.close(NEVER);
  }

  determineCycleState(): CycleState {
    if (this.isInPreparation()) {
      return CycleState.PREPARATION;
    } else {
      return CycleState.ACTIVE;
    }
  }

  isInPreparation(): boolean {
      const date: Date = this.cycleForm.get('startDate').value;

      return date.setHours(0, 0, 0, 0) > new Date().setHours(0, 0, 0, 0);
  }

  createCycle(): void {
    const startDate: Date = this.cycleForm.get('startDate').value;
    const endDate: Date = this.cycleForm.get('endDate').value;
    const isVisible: boolean = this.cycleForm.get ('isVisible').value;
    const cycle: CycleDto = {
      name: this.cycleForm.get('name').value,
      plannedStartDate:
        [Number(startDate.getFullYear()), Number(startDate.getMonth()) + 1, Number(startDate.getDate())],
      plannedEndDate:
        [Number(endDate.getFullYear()), Number(endDate.getMonth()) + 1, Number(endDate.getDate())],
      companyIds: [this.formData.company.id],
      cycleState: this.determineCycleState(), isVisible
    };
    this.dialogRef.close(
      this.cycleMapper.cloneCycleFromCycleId$(
        this.formData.company.cycleId, this.cycleMapper.mapCycleToCycleUnit(cycle)));
  }
}
