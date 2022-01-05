import { AbstractControl, ValidationErrors } from '@angular/forms';
import { AbstractValidator, register } from '../abstract-validator';
import { TranslateService } from '@ngx-translate/core';

export const currentHigherThanEndError: ValidationErrors = {
  currentHigherThanEndError: true
};

@register
export class CurrentHigherThanEndValidator extends AbstractValidator {

  constructor(private translate: TranslateService) {
    super(
      translate.instant('current-higher-than-end-validator-function.message'),
      currentHigherThanEndError);
  }

  static Validate(control: AbstractControl): ValidationErrors {
    const current: number = control.get('current').value;
    const end: number = control.get('end').value;
    const start: number = control.get('start').value;

    if ((current > end && end > start) || (current < end && end < start)) {
      return currentHigherThanEndError;
    }
  }
}
