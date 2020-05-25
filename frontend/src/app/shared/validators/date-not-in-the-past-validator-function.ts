import { AbstractControl, ValidatorFn } from '@angular/forms';
import { dateNoInThePastError } from './errors/date-not-in-the-past-error';

export const dateNotInThePastValidatorFunction: ValidatorFn = (control: AbstractControl): { [key: string]: boolean } => {

  const date: any = control.value;
  if (date < new Date().setHours(0, 0, 0, 0)) {
    return dateNoInThePastError;
  }
};
