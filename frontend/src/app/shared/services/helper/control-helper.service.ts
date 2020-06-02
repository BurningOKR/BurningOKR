import { AbstractControl, FormGroup } from '@angular/forms';
import { I18n } from '@ngx-translate/i18n-polyfill';
import { Injectable } from '@angular/core';
import { AbstractValidator, DateFormValidator } from '../../validators/abstract-validator';

@Injectable({
  providedIn: 'root'
})
export class ControlHelperService {

  validators: AbstractValidator[] = [DateFormValidator];

  private requiredErrorMessage = this.i18n({
    id: 'requiredError',
    value: 'Pflichtfeld'
  });
  private maxLenghsErrorMessage = this.i18n({
    id: 'maxLengthError',
    value: 'Maximale Zeichenzahl überschritten'
  });
  private dateInThePastErrorMessage = this.i18n({
    id: 'dateInThePastError',
    description: 'Date is in the past',
    value: 'Das eingegebene Datum liegt in der Vergangenheit'
  });
  private invalidFormValueErrorMessage = this.i18n({
    id: 'invalidFormValueError',
    description: 'is shown when no other error gets catched but there still is an error.',
    value: 'kein gültiger Wert.'
  });

  constructor(private i18n: I18n) {
  }

  getErrorMessage(form: FormGroup, controlName: string): string {
    const control: AbstractControl = form.get(controlName);


    if (control.hasError('required')) {
      return this.requiredErrorMessage;
    } else if (control.hasError('maxlength')) {
      return this.maxLenghsErrorMessage;
    } else if (control.hasError('dateInThePast')) {
      return this.dateInThePastErrorMessage;
    } else {
      return this.invalidFormValueErrorMessage;
    }
  }


  get(control: AbstractControl): string {
    const validator: AbstractValidator = this.validators
      .find(val => control.hasError(val.getErrorCode()));
    if (validator) {
      return validator.getErrorMessage();
    } else {
      return this.invalidFormValueErrorMessage;
    }
  }
}
