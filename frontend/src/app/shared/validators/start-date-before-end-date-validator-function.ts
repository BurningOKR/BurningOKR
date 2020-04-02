import { AbstractControl, ValidatorFn } from '@angular/forms';
export const startDateBeforeEndDateValidatorFunction: ValidatorFn = (control: AbstractControl): { [key: string]: boolean } => {
  const startDateControl: AbstractControl = control.get('startDate');
  const endDateControl: AbstractControl = control.get('endDate');

  if (startDateControl.touched || endDateControl.touched) {
    const startDate: any = startDateControl.value;
    const endDate: any = endDateControl.value;
    if (startDate >= endDate) {
      return {
        startDateNotBeforeEndDate: true
      };
    }

    return undefined;
  }
};
