import { ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { AbstractValidator, register } from '../abstract-validator';
import { TranslateService } from '@ngx-translate/core';

export const maxLengthError: ValidationErrors = {
  maxLengthError: true
};

@register
export class MaxLengthValidator extends AbstractValidator {

  constructor(private translate: TranslateService) {
    super(
      translate.instant('max-length-validator.message'),
      maxLengthError);
  }

  static Validate(max: number): ValidatorFn {
    return Validators.maxLength(max);
  }
}
