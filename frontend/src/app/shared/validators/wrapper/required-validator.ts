import { AbstractControl, ValidationErrors, Validators } from '@angular/forms';
import { AbstractValidator, register } from '../abstract-validator';
import { I18n } from '@ngx-translate/i18n-polyfill';

export const requiredError: ValidationErrors = {
  required: true
};

@register
export class RequiredValidator extends AbstractValidator {

  constructor(private i18n: I18n) {
    super(i18n({
      id: 'requiredError',
      description: 'Required Field',
      value: 'Pflichtfeld'
    }), requiredError);
  }

  static Validate(control: AbstractControl): ValidationErrors {
    return Validators.required(control);
  }
}
