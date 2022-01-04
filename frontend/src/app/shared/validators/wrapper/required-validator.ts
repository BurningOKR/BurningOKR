import { AbstractControl, ValidationErrors, Validators } from '@angular/forms';
import { AbstractValidator, register } from '../abstract-validator';
import { TranslateService } from '@ngx-translate/core';

export const requiredError: ValidationErrors = {
  required: true
};

@register
export class RequiredValidator extends AbstractValidator {

  constructor(private translate: TranslateService) {
    super(
      translate.instant('required-validator.message'),
      requiredError);
  }

  static Validate(control: AbstractControl): ValidationErrors {
    return Validators.required(control);
  }
}
