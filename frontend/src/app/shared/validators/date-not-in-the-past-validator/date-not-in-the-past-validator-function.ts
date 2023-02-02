import { AbstractControl, ValidationErrors } from '@angular/forms';
import { AbstractValidator, register } from '../abstract-validator';
import { TranslateService } from '@ngx-translate/core';

export const dateIsInThePastError: ValidationErrors = {
  dateNoInThePastError: true,
};

@register
export class DateNotInThePastValidator extends AbstractValidator {

  constructor(private translate: TranslateService) {
    super(
      translate.instant('date-not-in-the-past-validator-function.message'),
      dateIsInThePastError,
    );
  }

  static Validate(control: AbstractControl): ValidationErrors {
    const date: any = control.value;
    if (date < new Date().setHours(0, 0, 0, 0)) {
      return dateIsInThePastError;
    }
  }
}
