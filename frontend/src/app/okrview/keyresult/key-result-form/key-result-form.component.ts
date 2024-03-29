import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ViewKeyResult } from '../../../shared/model/ui/view-key-result';
import { FormArray, FormControl, FormGroup, Validators } from '@angular/forms';
import { KeyResultMapper } from '../../../shared/services/mapper/key-result.mapper';
import { Unit } from '../../../shared/model/api/unit.enum';
import { CurrentHigherThanEndValidator } from '../../../shared/validators/current-higher-than-end-validator/current-higher-than-end-validator-function';
import { StartDateNotEqualEndDateValidator } from '../../../shared/validators/start-not-equal-end-validator/start-not-equal-end-validator-function';
import { TranslateService } from '@ngx-translate/core';

interface KeyResultFormData {
  keyResult?: ViewKeyResult;
  objectiveId?: number;
}

@Component({
  selector: 'app-key-result-form',
  templateUrl: './key-result-form.component.html',
  styleUrls: ['./key-result-form.component.scss'],
})
export class KeyResultFormComponent implements OnInit {

  keyResult: ViewKeyResult;
  keyResultForm: FormGroup;
  unitEnum = Unit;
  title: string;

  constructor(
    private dialogRef: MatDialogRef<KeyResultFormComponent>,
    private translate: TranslateService,
    private keyResultMapper: KeyResultMapper,
    @Inject(MAT_DIALOG_DATA) private formData: KeyResultFormData,
  ) {
  }

  ngOnInit(): void {
    this.keyResultForm = new FormGroup({
      keyResult: new FormControl('', [Validators.required, Validators.maxLength(255)]),
      current: new FormControl(0, [Validators.required, Validators.min(0)]),
      end: new FormControl(10, [Validators.required, Validators.min(0)]),
      start: new FormControl(0, [Validators.required, Validators.min(0)]),
      unit: new FormControl(Unit.NUMBER, [Validators.required]),
      description: new FormControl('', [Validators.maxLength(255)]),
      viewKeyResultMilestones: new FormArray([]),
    }, [StartDateNotEqualEndDateValidator.Validate, CurrentHigherThanEndValidator.Validate]);

    if (this.formData.keyResult) {
      this.keyResult = this.formData.keyResult;
      this.keyResultForm.patchValue(this.formData.keyResult);
    }

    const editText: string = this.translate.instant('key-result-form.dialog.edit');
    const createText: string = this.translate.instant('key-result-form.dialog.create');

    this.title = `Key Result ${this.keyResult ? editText : createText}`;
  }

  getViewUnitKeys(): string[] {
    return Object.keys(Unit);
  }

  getViewUnit(unit: string): string {
    if (this.unitEnum[unit] === '#') {
      return this.translate.instant('key-result-form.amount-units');
    } else {
      return this.unitEnum[unit];
    }
  }

  closeDialog(): void {
    this.dialogRef.close(undefined);
  }

  saveKeyResult(): void {
    const keyResult: ViewKeyResult = this.formData.keyResult;

    if (keyResult) {
      keyResult.keyResult = this.keyResultForm.get('keyResult').value;
      keyResult.current = this.keyResultForm.get('current').value;
      keyResult.end = this.keyResultForm.get('end').value;
      keyResult.start = this.keyResultForm.get('start').value;
      keyResult.description = this.keyResultForm.get('description').value;
      keyResult.unit = this.keyResultForm.get('unit').value;
      keyResult.viewKeyResultMilestones = this.keyResultForm.get('viewKeyResultMilestones').value;

      this.dialogRef.close(this.keyResultMapper.putKeyResult$(keyResult));
    } else {
      const formData: ViewKeyResult = this.keyResultForm.getRawValue();

      const newKeyResult: ViewKeyResult = new ViewKeyResult(
        undefined,
        formData.start,
        formData.current,
        formData.end,
        formData.unit,
        formData.keyResult,
        formData.description,
        undefined,
        undefined,
        formData.viewKeyResultMilestones,
      );

      this.dialogRef.close(this.keyResultMapper.postKeyResult$(this.formData.objectiveId, newKeyResult));
    }
  }

}
