import { ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { AbstractValidator, register } from '../abstract-validator';
import { I18n } from '@ngx-translate/i18n-polyfill';

export const maxLengthError: ValidationErrors = {
  maxLengthError: true
};

@register
export class MaxLengthValidator extends AbstractValidator {

  constructor(private i18n: I18n) {
    super(i18n({
      id: 'maxLengthError',
      description: 'Max Length',
      value: 'Maximale Zeichenzahl überschritten'
    }), maxLengthError);
  }

  static Validate(max: number): ValidatorFn {
    return Validators.maxLength(max);
  }
}
