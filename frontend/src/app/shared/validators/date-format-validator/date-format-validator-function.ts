import { AbstractControl, ValidationErrors } from '@angular/forms';
import { I18n } from '@ngx-translate/i18n-polyfill';
import { AbstractValidator, register } from '../abstract-validator';

export const dateFormatError: ValidationErrors = {
  dateFormatError: true
};

@register
export class DateFormValidator extends AbstractValidator {

  constructor(private i18n: I18n) {
    super(i18n({
      id: 'dateFormatError',
      description: 'Date is in invalid format from DD.MM.YYYY',
      value: 'Der eingegebene Wert ist kein gültiges Datum in der Form TT.MM.JJJJ'
    }), dateFormatError);
  }

  static Validate(control: AbstractControl): ValidationErrors {
    const date: string = control.value;
    const dates: string[] = date.split('.');
    if (date === null || date === '') {
      return dateFormatError;
    }
    if (Number(dates[0]) >= 31 && Number(dates[1]) >= 12) {
        return dateFormatError;
      }
    if (Number(dates[0]) <= 0 && Number(dates[1]) <= 0) {
        return dateFormatError;
    }
  }
}
