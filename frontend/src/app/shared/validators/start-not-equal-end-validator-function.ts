import { AbstractControl, ValidatorFn } from '@angular/forms';

export const startNotEqualEndValidatorFunction: ValidatorFn = (control: AbstractControl): { [key: string]: boolean } => {
  const startValue: any = control.get('start').value;
  const endValue: any = control.get('end').value;
  if (startValue === endValue) {
    return {
      startEqualsEnd: true
    };
  } else {
    return undefined;
  }
};
