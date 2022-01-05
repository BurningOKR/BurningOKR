import { ValidationErrorService } from './validation-error.service';
import { TestBed } from '@angular/core/testing';
import { FormControl } from '@angular/forms';
import { RequiredValidator } from '../../validators/wrapper/required-validator';
import { MaterialTestingModule } from '../../../testing/material-testing.module';
import { AbstractValidator, getValidators } from '../../validators/abstract-validator';
import { TranslateService } from '@ngx-translate/core';

describe('ValidationErrorService', () => {

  let service: ValidationErrorService;
  let translate: TranslateService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ MaterialTestingModule ],
    });
    service = TestBed.inject(ValidationErrorService);
    translate = TestBed.inject(TranslateService);
  });

  it('should return default error code', () => {
    const control: FormControl = new FormControl();
    spyOn(service, 'getDefaultErrorMessage').and.returnValue('kein gültiger Wert.');

    const actual: string = service.getErrorMessage(control);

    expect(actual)
      .toEqual('kein gültiger Wert.');

  });

  it('should return specific error code', () => {
    const control: FormControl = new FormControl(null, [RequiredValidator.Validate]);
    const validator: AbstractValidator = getValidators(translate)
      .find(val => control.hasError(val.getErrorCode()));
    spyOn(validator, 'getErrorMessage').and.returnValue('Pflichtfeld');

    const actual: string = service.getErrorMessage(control);

    expect(actual)
      .toEqual('Pflichtfeld');

  });
});
