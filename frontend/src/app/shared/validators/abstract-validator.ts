import { ValidatorFn } from '@angular/forms';

export abstract class AbstractValidator {

  private _errorMessage: string = '';
  constructor(errorMessage: string )
  {}
  get ErrorMessage(): string {
    return this._errorMessage;
  }

  protected SetErrorMessage(val: string): void {
    this._errorMessage = val;
  }

  getValidatorFn(): ValidatorFn;
}
