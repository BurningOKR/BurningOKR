import { AbstractControl, ValidatorFn } from '@angular/forms';

export const currentHigherThanEndValidatorFunction: ValidatorFn = (control: AbstractControl): { [key: string]: boolean } => {
  const current: number = control.get('current').value;
  const end: number = control.get('end').value;

  if (current > end) {
    return {currentHigherThanEndError: true};
  }

};
