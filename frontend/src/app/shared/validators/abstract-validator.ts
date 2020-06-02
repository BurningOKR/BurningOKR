import { ValidationErrors } from '@angular/forms';
import { I18n } from '@ngx-translate/i18n-polyfill';
import { dateFormatError } from './errors/date-format-error';

export abstract class AbstractValidator {

  private _errorMessage: string;
  private _errorCode: string;

  protected constructor(errorMessage: string, errorCode: ValidationErrors) {
    this._errorMessage = errorMessage;
    this._errorCode = Object.keys(errorCode)[0];
  }

  getErrorMessage(): string {
    return this._errorMessage;
  }

  protected setErrorMessage(val: string): void {
    this._errorMessage = val;
  }

  getErrorCode(): string {
    return this._errorCode;
  }

  protected setErrorCode(val: string): void {
    this._errorCode = val;
  }


}

export class DateFormValidator extends AbstractValidator {

  constructor(private i18n: I18n) {
    super(i18n({
      id: 'dateFormatError',
      description: 'Date is in invalid format from DD.MM.YYYY',
      value: 'Der eingegebene Wert ist kein gültiges Datum in der Form TT.MM.JJJJ'
    }), dateFormatError);

  }
}

