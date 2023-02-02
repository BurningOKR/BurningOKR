import { AbstractControl } from '@angular/forms';
import { Injectable } from '@angular/core';
import { AbstractValidator, getValidators } from '../../validators/abstract-validator';
import { TranslateService } from '@ngx-translate/core';

@Injectable({
  providedIn: 'root'
})
export class ValidationErrorService {

  constructor(private translate: TranslateService) {
  }

  getErrorMessage(control: AbstractControl): string {
    const validator: AbstractValidator = getValidators(this.translate)
      .find(val => control.hasError(val.getErrorCode()));
    if (validator) {
      return validator.getErrorMessage();
    } else {
      return this.translate.instant('validation-error.default-error-message');
    }
  }
}
