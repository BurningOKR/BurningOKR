import { AbstractControl, ValidationErrors } from '@angular/forms';
import { AbstractValidator, register } from '../abstract-validator';
import { TranslateService } from '@ngx-translate/core';

export const passwortMismatch: ValidationErrors = {
  passwortMismatch: true,
};

@register
export class PasswordsMatchValidator extends AbstractValidator {

  constructor(private translate: TranslateService) {
    super(
      translate.instant('passwords-match-validator-function.message'),
      passwortMismatch,
    );
  }

  static Validate(control: AbstractControl): ValidationErrors {
    const password: string = control.get('newPassword').value;
    const newPasswordRepetition: string = control.get('newPasswordRepetition').value;

    if (password !== newPasswordRepetition) {
      return { passwordDoNotMatchError: true };
    }
  }
}
