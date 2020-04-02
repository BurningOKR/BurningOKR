import { AbstractControl, ValidatorFn } from '@angular/forms';

export const dateNotInThePastValidatorFunction: ValidatorFn = (control: AbstractControl): { [key: string]: boolean } => {

  const date: any = control.value;
  if (date < new Date().setHours(0, 0, 0, 0)) {
    return {
      dateInThePast: true
    };
  }
};
