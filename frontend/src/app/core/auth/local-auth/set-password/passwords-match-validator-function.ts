import { AbstractControl, ValidatorFn } from '@angular/forms';

export const passwordMatchValidatorFunction: ValidatorFn = (control: AbstractControl): {[key: string]: boolean} => {
  const password: string = control.get('newPassword').value;
  const newPasswordRepetition: string = control.get('newPasswordRepetition').value;

  if (password !== newPasswordRepetition) {
    return {passwordDoNotMatchError: true};
  }
};
