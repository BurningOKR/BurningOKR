import { FormControl } from '@angular/forms';
import { I18n } from '@ngx-translate/i18n-polyfill';
import { Injectable } from '@angular/core';
import { AbstractValidator, getValidators } from '../../validators/abstract-validator';

@Injectable({
  providedIn: 'root'
})
export class ValidationErrorService {

  // validators: AbstractValidator[] = [DateFormValidator];

  private requiredErrorMessage = this.i18n({
    id: 'requiredError',
    value: 'Pflichtfeld'
  });
  private maxLenghsErrorMessage = this.i18n({
    id: 'maxLengthError',
    value: 'Maximale Zeichenzahl überschritten'
  });
  private invalidFormValueErrorMessage = this.i18n({
    id: 'invalidFormValueError',
    description: 'is shown when no other error gets catched but there still is an error.',
    value: 'kein gültiger Wert.'
  });

  constructor(private i18n: I18n) {
  }

  getErrorMessage(control: FormControl): string {
    const validator: AbstractValidator = getValidators(this.i18n)
      .find(val => control.hasError(val.getErrorCode()));
    if (validator) {
      return validator.getErrorMessage();
    } else {
      return this.invalidFormValueErrorMessage;
    }
  }
}
