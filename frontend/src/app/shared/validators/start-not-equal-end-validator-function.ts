import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';
import { startEqualsEndError } from './errors/start-not-equal-end-validator-error';

export const startNotEqualEndValidatorFunction: ValidatorFn = (control: AbstractControl): ValidationErrors => {
  const startValue: any = control.get('start').value;
  const endValue: any = control.get('end').value;
  if (startValue === endValue) {
    return startEqualsEndError;
  } else {
    return undefined;
  }
};
