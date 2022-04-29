import { Component, Inject, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { TranslateService } from '@ngx-translate/core';
import { NEVER } from 'rxjs';
import { Observable } from 'rxjs/internal/Observable';
import { take } from 'rxjs/operators';
import { CycleDto } from '../../shared/model/api/cycle.dto';
import { CycleDialogData } from '../../shared/model/ui/cycle-dialog-data';
import { CycleState, CycleUnit } from '../../shared/model/ui/cycle-unit';
import { CompanyMapper } from '../../shared/services/mapper/company.mapper';
import { CycleMapper } from '../../shared/services/mapper/cycle.mapper';
import { DateMapper } from '../../shared/services/mapper/date.mapper';
import { DateFormValidator } from '../../shared/validators/date-format-validator/date-format-validator-function';
import {
  DateNotInThePastValidator,
} from '../../shared/validators/date-not-in-the-past-validator/date-not-in-the-past-validator-function';
import {
  DateNotInRangeOfAnotherCycleValidator,
} from
'../../shared/validators/date-range-in-range-within-another-dates-validator/date-range-in-range-within-another-dates-validator-function';
import {
  StartDateBeforeEndDateValidator,
} from '../../shared/validators/start-date-before-end-date-validator/start-date-before-end-date-validator-function';

@Component({
  selector: 'app-cycle-creation-form',
  templateUrl: './cycle-creation-form.component.html',
  styleUrls: ['./cycle-creation-form.component.scss'],
})
export class CycleCreationFormComponent implements OnInit {
  cycleForm: FormGroup;
  cycles: CycleUnit[];
  title$: Observable<string>;
  saveAndCloseLabel: string;

  firstAvailableDate: Date;
  minimumCycleDuration: number = 3;

  constructor(private dialogRef: MatDialogRef<CycleCreationFormComponent>,
              private cycleMapper: CycleMapper,
              private companyService: CompanyMapper,
              private dateMapper: DateMapper,
              private translate: TranslateService,
              @Inject(MAT_DIALOG_DATA) public formData: CycleDialogData) {
  }

  ngOnInit(): void {
    this.formData.cycles$.pipe(take(1))
      .subscribe((cycles: CycleUnit[]) => {
        this.cycles = cycles;
        this.cycleForm = new FormGroup({
          name: new FormControl('', [Validators.required, Validators.maxLength(255)]),
          startDate: new FormControl('', [DateFormValidator.Validate, DateNotInThePastValidator.Validate]),
          endDate: new FormControl('', [DateFormValidator.Validate]),
          isVisible: new FormControl(true),
        }, [StartDateBeforeEndDateValidator.Validate,
          DateNotInRangeOfAnotherCycleValidator.Validate(this.cycles),
        ]);
      });

    this.title$ = this.translate.stream('cycle-creation-form.creation-dialog.title');
    this.saveAndCloseLabel = this.translate.instant('cycle-creation-form.creation-dialog.save');
    this.firstAvailableDate = new Date();
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
    const date: Date = this.dateMapper.mapToDate(this.cycleForm.get('startDate').value);

    return date.setHours(0, 0, 0, 0) > new Date().setHours(0, 0, 0, 0);
  }

  createCycle(): void {
    const startDate: Date = this.dateMapper.mapToDate(this.cycleForm.get('startDate').value);
    const endDate: Date = this.dateMapper.mapToDate(this.cycleForm.get('endDate').value);
    const isVisible: boolean = this.cycleForm.get('isVisible').value;
    const cycle: CycleDto = {
      name: this.cycleForm.get('name').value,
      plannedStartDate:
        [Number(startDate.getFullYear()), Number(startDate.getMonth()) + 1, Number(startDate.getDate())],
      plannedEndDate:
        [Number(endDate.getFullYear()), Number(endDate.getMonth()) + 1, Number(endDate.getDate())],
      companyIds: [this.formData.company.id],
      cycleState: this.determineCycleState(), isVisible,
    };
    this.dialogRef.close(
      this.cycleMapper.cloneCycleFromCycleId$(
        this.formData.company.cycleId, this.cycleMapper.mapCycleToCycleUnit(cycle)));
  }

  dateChangeHandler(event) {
    const date: Date = event.value.toDate();
    date.setDate(date.getDate() + this.cycleMinDuration);
    this.firstAvailableDate = date;
  }
}
