import { Component, Inject, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { CycleState, CycleUnit } from '../../shared/model/ui/cycle-unit';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { CycleMapper } from '../../shared/services/mapper/cycle.mapper';
import { NEVER, Subscription } from 'rxjs';
import { CycleDto } from '../../shared/model/api/cycle.dto';
import { CompanyMapper } from '../../shared/services/mapper/company.mapper';
import { DateNotInRangeOfAnotherCycleValidator } from '../../shared/validators/date-range-in-range-within-another-dates-validator/date-range-in-range-within-another-dates-validator-function';
import { CycleDialogData } from '../../shared/model/ui/cycle-dialog-data';
import { DateFormValidator } from '../../shared/validators/date-format-validator/date-format-validator-function';
import { DateNotInThePastValidator } from '../../shared/validators/date-not-in-the-past-validator/date-not-in-the-past-validator-function';
import { StartDateBeforeEndDateValidator } from '../../shared/validators/start-date-before-end-date-validator/start-date-before-end-date-validator-function';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-cycle-creation-form',
  templateUrl: './cycle-creation-form.component.html',
  styleUrls: ['./cycle-creation-form.component.scss']
})
export class CycleCreationFormComponent implements OnInit, OnDestroy {

  subscriptions: Subscription[] = [];
  cycleForm: FormGroup;
  cycles: CycleUnit[];
  title: string;
  saveAndCloseLabel: string;

  constructor(private dialogRef: MatDialogRef<CycleCreationFormComponent>,
              private cycleMapper: CycleMapper,
              private companyService: CompanyMapper,
              private translate: TranslateService,
              @Inject(MAT_DIALOG_DATA) public formData: CycleDialogData) {
  }

  ngOnInit(): void {
    this.subscriptions.push(this.formData.cycles$
      .subscribe((cycles: CycleUnit[]) => {
        this.cycles = cycles;
        this.cycleForm = new FormGroup({
          name: new FormControl('', [Validators.required, Validators.maxLength(255)]),
          startDate: new FormControl('', [DateFormValidator.Validate, DateNotInThePastValidator.Validate]),
          endDate: new FormControl('', [DateFormValidator.Validate]),
          isVisible: new FormControl(true)
        }, [StartDateBeforeEndDateValidator.Validate,
          DateNotInRangeOfAnotherCycleValidator.Validate(this.cycles)
        ]);
      }));

    this.subscriptions.push(this.translate.stream('cycle-creation-form.creation-dialog.title').subscribe(text => {
      this.title = text;
    }));
    this.subscriptions.push(this.translate.stream('cycle-creation-form.creation-dialog.save').subscribe(text => {
      this.saveAndCloseLabel = text;
    }));
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
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
    const isVisible: boolean = this.cycleForm.get('isVisible').value;
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
