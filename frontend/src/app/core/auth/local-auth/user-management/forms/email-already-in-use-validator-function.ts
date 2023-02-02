import { AbstractControl, ValidatorFn } from '@angular/forms';

export const emailAlreadyInUseValidatorFunction: (emails: string[]) =>
  ValidatorFn = (emailsOfUsers: string[]): ValidatorFn => {
  return (control: AbstractControl): { [key: string]: boolean } => {
    const newEmail: string = control.value.toLowerCase();

    for (const emailOfUser of emailsOfUsers) {
      if (newEmail === emailOfUser.toLowerCase()) {
        return {emailAlreadyInUse: true};
      }
    }
  };
};
