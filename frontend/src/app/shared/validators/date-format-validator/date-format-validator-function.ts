import { AbstractControl, ValidationErrors } from '@angular/forms';
import { AbstractValidator, register } from '../abstract-validator';
import { TranslateService } from '@ngx-translate/core';

export const dateFormatError: ValidationErrors = {
  dateFormatError: true,
};

@register
export class DateFormValidator extends AbstractValidator {

  constructor(private translate: TranslateService) {
    super(
      translate.instant('date-format-validator-function.message'),
      dateFormatError,
    );
  }

  static Validate(control: AbstractControl): ValidationErrors {
    const date: string = control.value;

    if (date === null || date === '') {
      return dateFormatError;
    }
  }
}
