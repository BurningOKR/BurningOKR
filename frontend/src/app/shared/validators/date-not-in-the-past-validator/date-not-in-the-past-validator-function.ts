import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';
import { AbstractValidator, register } from '../abstract-validator';
import { I18n } from '@ngx-translate/i18n-polyfill';

export const dateIsInThePastError: ValidationErrors = {
  dateNoInThePastError: true
};

@register
export class DateNotInThePastValidator extends AbstractValidator {

  constructor(private i18n: I18n) {
    super(i18n({
      id: 'dateInThePastError',
      description: 'Date is in the past',
      value: 'Das eingegebene Datum liegt in der Vergangenheit'
    }), dateIsInThePastError);
  }

  static Validate(control: AbstractControl): ValidationErrors {
    const date: any = control.value;
    if (date < new Date().setHours(0, 0, 0, 0)) {
      return dateIsInThePastError;
    }
  }
}
