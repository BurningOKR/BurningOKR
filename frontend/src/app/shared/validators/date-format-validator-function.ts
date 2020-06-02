import { AbstractControl, ValidatorFn } from '@angular/forms';
import { dateFormatError } from './errors/date-format-error';

export const dateFormatValidatorFunction: ValidatorFn = (control: AbstractControl): { [key: string]: boolean } => {
  const date: string = control.value;

  if (date === null || date === '') {
    return dateFormatError;
  }
};
