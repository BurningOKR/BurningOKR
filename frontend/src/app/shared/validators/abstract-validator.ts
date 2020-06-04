import { AbstractControl, ValidationErrors } from '@angular/forms';
import { I18n } from '@ngx-translate/i18n-polyfill';

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

type Constructor<T> = {
  new(...args: any[]): T;
  readonly prototype: T;
};
const validatorsConstructors: Constructor<AbstractValidator>[] = [];
let validators: AbstractValidator[] = null;

export function getValidators(i18n: I18n): AbstractValidator[] {
  if (!validators) {
    validators = [];
    validatorsConstructors.forEach(ctor => validators.push(new ctor(i18n)));
  }

  return validators;
}

export function register<T extends Constructor<AbstractValidator>>(ctor: T): void {
  validatorsConstructors.push(ctor);
}

export const dateFormatError: ValidationErrors = {
  dateFormatError: true
};

@register
export class DateFormValidatorImpl extends AbstractValidator {

  constructor(private i18n: I18n) {
    super(i18n({
      id: 'dateFormatError',
      description: 'Date is in invalid format from DD.MM.YYYY',
      value: 'Der eingegebene Wert ist kein gültiges Datum in der Form TT.MM.JJJJ'
    }), dateFormatError);
  }

  static Validate(control: AbstractControl): ValidationErrors {
    const date: string = control.value;

    if (date === null || date === '') {
      return dateFormatError;
    }
  }
}
