import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';
// import { dateFormatError } from './errors/date-format-error';

export const dateFormatValidatorFunction: ValidatorFn = (control: AbstractControl): ValidationErrors => {
  const date: string = control.value;

  if (date === null || date === '') {
    return {dateFormatError: true};
  }
};
