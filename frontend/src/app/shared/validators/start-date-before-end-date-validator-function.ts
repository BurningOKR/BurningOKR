import { AbstractControl, ValidatorFn } from '@angular/forms';
import { startDateBeforeEndDateError } from './errors/start-date-not-before-end-date-error';
export const startDateBeforeEndDateValidatorFunction: ValidatorFn = (control: AbstractControl): { [key: string]: boolean } => {
  const startDateControl: AbstractControl = control.get('startDate');
  const endDateControl: AbstractControl = control.get('endDate');

  if (startDateControl.touched || endDateControl.touched) {
    const startDate: any = startDateControl.value;
    const endDate: any = endDateControl.value;
    if (startDate >= endDate) {
      return startDateBeforeEndDateError;
    }

    return undefined;
  }
};
