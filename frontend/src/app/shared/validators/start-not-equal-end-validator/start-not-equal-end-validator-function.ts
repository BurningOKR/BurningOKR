import { AbstractControl, ValidationErrors } from '@angular/forms';
import { AbstractValidator, register } from '../abstract-validator';
import { TranslateService } from '@ngx-translate/core';

export const startEqualsEndError: ValidationErrors = {
  wrongPassword: true
};

@register
export class StartDateNotEqualEndDateValidator extends AbstractValidator {

  constructor(private translate: TranslateService) {
    super(
      translate.instant('start-not-equal-end-validator-function.message'),
      startEqualsEndError);
  }

  static Validate(control: AbstractControl): ValidationErrors {
    const startValue: any = control.get('start').value;
    const endValue: any = control.get('end').value;
    if (startValue === endValue) {
      return startEqualsEndError;
    } else {
      return undefined;
    }
  }
}
