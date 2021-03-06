import { ValidationErrorService } from './validation-error.service';
import { TestBed } from '@angular/core/testing';
import { I18n } from '@ngx-translate/i18n-polyfill';
import { FormControl, Validators } from '@angular/forms';
import { dateIsInThePastError } from '../../validators/date-not-in-the-past-validator/date-not-in-the-past-validator-function';
import { TRANSLATIONS, TRANSLATIONS_FORMAT } from '@angular/core';
import { RequiredValidator } from '../../validators/wrapper/required-validator';

describe('ValidationErrorService', () => {

  let service: ValidationErrorService;
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [],
      providers: [
        {provide: TRANSLATIONS, useValue: 'xlf'},
        {provide: TRANSLATIONS_FORMAT, useValue: 'xlf'},
        I18n,
      ]
    });
    service = TestBed.get(ValidationErrorService);
  });

  it('should return default error code', () => {
    const control: FormControl = new FormControl();

    const actual: string = service.getErrorMessage(control);

    expect(actual)
      .toEqual('kein gültiger Wert.');

  });

  it('should return specific error code', () => {
    const control: FormControl = new FormControl(null, [RequiredValidator.Validate]);

    const actual: string = service.getErrorMessage(control);

    expect(actual)
      .toEqual('Pflichtfeld');

  });
});
