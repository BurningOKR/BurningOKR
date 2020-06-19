import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';
import { AbstractValidator, register } from '../abstract-validator';
import { I18n } from '@ngx-translate/i18n-polyfill';
import { startDateNotBeforeEndDate } from '../start-date-before-end-date-validator/start-date-before-end-date-validator-function';

export const startEqualsEndError: ValidationErrors = {
  wrongPassword: true
};

@register
export class StartDateNotEqualEndDateValidator extends AbstractValidator {

  constructor(private i18n: I18n) {
    super(i18n({
      id: 'startDateNotBeforeEndDate',
      description: 'The startdate ist after the enddate',
      value: 'Das Anfangsdatum ist nach dem Enddatum'
    }), startEqualsEndError);
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
