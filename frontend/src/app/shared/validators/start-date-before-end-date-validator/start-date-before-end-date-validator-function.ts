import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';
import { AbstractValidator, register } from '../abstract-validator';
import { I18n } from '@ngx-translate/i18n-polyfill';

export const startDateNotBeforeEndDate: ValidationErrors = {
  startDateNotBeforeEndDate: true
};

@register
export class StartDateBeforeEndDateValidator extends AbstractValidator {

  constructor(private i18n: I18n) {
    super(i18n({
      id: 'startDateNotBeforeEndDate',
      description: 'The startdate ist after the enddate',
      value: 'Das Anfangsdatum ist nach dem Enddatum'
    }), startDateNotBeforeEndDate);
  }

  static Validate(control: AbstractControl): ValidationErrors {
    const startDateControl: AbstractControl = control.get('startDate');
    const endDateControl: AbstractControl = control.get('endDate');

    if (startDateControl.touched || endDateControl.touched) {
      const startDate: any = startDateControl.value;
      const endDate: any = endDateControl.value;
      if (startDate >= endDate) {
        return startDateNotBeforeEndDate;
      }

      return undefined;
    }
  }
}
