import { AbstractControl, ValidationErrors } from '@angular/forms';
import { AbstractValidator, register } from '../abstract-validator';
import { TranslateService } from '@ngx-translate/core';

export const startDateNotBeforeEndDate: ValidationErrors = {
  startDateNotBeforeEndDate: true
};

@register
export class StartDateBeforeEndDateValidator extends AbstractValidator {

  constructor(private translate: TranslateService) {
    super(
      translate.instant('start-date-before-end-date-validator-function.message'),
      startDateNotBeforeEndDate);
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
