import { AbstractControl, ValidationErrors } from '@angular/forms';
import { AbstractValidator, register } from '../abstract-validator';
import { I18n } from '@ngx-translate/i18n-polyfill';

export const passwortMismatch: ValidationErrors = {
  passwortMismatch: true
};

@register
export class PasswordsMatchValidator extends AbstractValidator {

  constructor(private i18n: I18n) {
    super(i18n({
      id: 'PasswordsMatchValidator',
      description: 'The passwords do not match',
      value: 'Die Passwörter stimmen nicht überein.'
    }), passwortMismatch);
  }

  static Validate(control: AbstractControl): ValidationErrors {
    const password: string = control.get('newPassword').value;
    const newPasswordRepetition: string = control.get('newPasswordRepetition').value;

    if (password !== newPasswordRepetition) {
      return {passwordDoNotMatchError: true};
    }
  }
}
