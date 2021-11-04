import { ValidationErrors } from '@angular/forms';
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

  getErrorCode(): string {
    return this._errorCode;
  }

  protected setErrorMessage(val: string): void {
    this._errorMessage = val;
  }

  protected setErrorCode(val: string): void {
    this._errorCode = val;
  }

}

const validatorsConstructors: Constructor<AbstractValidator>[] = [];
let validators: AbstractValidator[] = null;

interface Constructor<T> {
  readonly prototype: T;
  new(...args: any[]): T;
}

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
