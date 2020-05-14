import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ViewKeyResult } from '../../../shared/model/ui/view-key-result';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { KeyResultMapper } from '../../../shared/services/mapper/key-result.mapper';
import { Unit } from '../../../shared/model/api/unit.enum';
import { ControlHelperService } from '../../../shared/services/helper/control-helper.service';
import { startNotEqualEndValidatorFunction } from '../../../shared/validators/start-not-equal-end-validator-function';
import { I18n } from '@ngx-translate/i18n-polyfill';

interface KeyResultFormData {
  keyResult?: ViewKeyResult;
  objectiveId?: number;
}

@Component({
  selector: 'app-key-result-form',
  templateUrl: './key-result-form.component.html',
  styleUrls: ['./key-result-form.component.scss'],
})
export class KeyResultFormComponent {
  keyResult: ViewKeyResult;
  keyResultForm: FormGroup;
  unitEnum = Unit;
  title: string;
  getErrorMessage = this.controlHelperService.getErrorMessage;

  constructor(private dialogRef: MatDialogRef<KeyResultFormComponent>,
              private i18n: I18n,
              private controlHelperService: ControlHelperService,
              private keyResultMapper: KeyResultMapper, @Inject(MAT_DIALOG_DATA) private formData: KeyResultFormData) {
    this.keyResultForm = new FormGroup({
      keyResult: new FormControl('', [Validators.required, Validators.maxLength(255)]),
      current: new FormControl(0, [Validators.required, Validators.min(0)]),
      end: new FormControl(10, [Validators.required, Validators.min(0)]),
      start: new FormControl(0, [Validators.required, Validators.min(0)]),
      unit: new FormControl(Unit.NUMBER, [Validators.required]),
      description: new FormControl('', [Validators.maxLength(255)])
    }, [startNotEqualEndValidatorFunction]);

    if (this.formData.keyResult) {
      this.keyResult = this.formData.keyResult;
      this.keyResultForm.patchValue(this.formData.keyResult);
    }

    const editText: string = this.i18n({
      id: 'edit',
      value: 'bearbeiten'
    });

    const createText: string = this.i18n({
      id: 'create',
      value: 'erstellen'
    });

    this.title = `Key Result ${this.keyResult ? editText : createText}`;
  }

  getViewUnitKeys(): string[] {
    return Object.keys(Unit);
  }

  getViewUnit(unit: string): string {
    if (this.unitEnum[unit] === '') {
      return this.i18n({
        id: 'amount',
        value: 'Anzahl'
      });
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
        undefined
      );

      this.dialogRef.close(this.keyResultMapper.postKeyResult$(this.formData.objectiveId, newKeyResult));
    }
  }

}
